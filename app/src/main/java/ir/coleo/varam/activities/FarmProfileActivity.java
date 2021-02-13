package ir.coleo.varam.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.tabs.AddLivestockActivity;
import ir.coleo.varam.adapters.GridViewAdapterCowInFarmProfile;
import ir.coleo.varam.adapters.RecyclerViewAdapterNextVisitFarmProfile;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.CowWithLastVisit;
import ir.coleo.varam.database.models.FarmWithNextVisit;
import ir.coleo.varam.database.models.MyReport;
import ir.coleo.varam.database.models.NextVisit;
import ir.coleo.varam.database.models.main.Cow;
import ir.coleo.varam.database.models.main.Drug;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.MyDate;

import static ir.coleo.varam.R.string.more_info;
import static ir.coleo.varam.R.string.next_visit;

public class FarmProfileActivity extends AppCompatActivity {

    private TextView title;
    private TextView visitTitle;
    private TextView showerCount;
    private TextView bedType;
    private TextView scoreMethod;
    private TextView birthCount;
    private TextView nextVisit;
    private ImageView bookmark;
    private GridView cowsGridView;
    private RecyclerView nextVisitView;
    private RecyclerViewAdapterNextVisitFarmProfile mAdapter;
    private ImageView menu;
    private ConstraintLayout menuLayout;
    private ImageView outside;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock_profile);

        menu = findViewById(R.id.dropdown_menu);
        menuLayout = findViewById(R.id.menu_layout);
        outside = findViewById(R.id.outside);
        bedType = findViewById(R.id.bed_type_value);
        scoreMethod = findViewById(R.id.score_type_value);
        title = findViewById(R.id.title_livestrok);
        visitTitle = findViewById(R.id.next_visit_title);
        birthCount = findViewById(R.id.count_value);
        showerCount = findViewById(R.id.system_value);
        nextVisit = findViewById(R.id.next_visit_value);
        bookmark = findViewById(R.id.bookmark_image);
        cowsGridView = findViewById(R.id.cows_grid);
        nextVisitView = findViewById(R.id.next_visit_lists);
        ImageView exit = findViewById(R.id.back_icon);
        exit.setOnClickListener(view -> finish());
        Constants.setImageBackBorder(this, exit);

        id = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.FARM_ID);
        nextVisitView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        nextVisitView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterNextVisitFarmProfile(new ArrayList<>(), this);
        nextVisitView.setAdapter(mAdapter);

        menu.setOnClickListener(view -> showMenu());
        outside.setOnClickListener(view -> hideMenu());
        ConstraintLayout edit = findViewById(R.id.item_one);
        ConstraintLayout remove = findViewById(R.id.item_two);
        ConstraintLayout share = findViewById(R.id.item_three);
        edit.setOnClickListener(view -> {
            hideMenu();
            Intent intent = new Intent(this, AddLivestockActivity.class);
            intent.putExtra(Constants.FARM_ID, id);
            intent.putExtra(Constants.ADD_FARM_MODE, Constants.EDIT_FARM);
            startActivity(intent);
        });
        remove.setOnClickListener(view -> {
            MyDao dao = DataBase.getInstance(this).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                Farm farm = dao.getFarm(id);
                List<Cow> cows = dao.getAllCowOfFarm(id);
                for (Cow cow : cows) {
                    for (Report report : dao.getAllReportOfCow(cow.getId()))
                        dao.deleteReport(report);
                    dao.deleteCow(cow);
                }
                dao.deleteFarm(farm);
                runOnUiThread(() -> {
                    hideMenu();
                    finish();
                });
            });
        });
        share.setOnClickListener(view -> {
            export();
            hideMenu();
        });


    }

    private void showMenu() {
        outside.setVisibility(View.VISIBLE);
        menuLayout.setVisibility(View.VISIBLE);
        menu.setVisibility(View.INVISIBLE);
    }

    private void hideMenu() {
        menu.setVisibility(View.VISIBLE);
        outside.setVisibility(View.GONE);
        menuLayout.setVisibility(View.GONE);
    }

    private void hideVisit() {
        visitTitle.setVisibility(View.GONE);
        nextVisitView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            FarmWithNextVisit farmWithNextVisit = dao.getFarmWithNextVisit(id);
            Farm farm = dao.getFarm(id);
            runOnUiThread(() -> {
                bedType.setText(farm.bedType);
                if (farm.scoreMethod) {
                    scoreMethod.setText(getString(R.string.three_level_text));
                } else {
                    scoreMethod.setText(getString(R.string.four_level_text));
                }
                bookmark.setOnClickListener(view -> {
                    farm.favorite = !farm.favorite;
                    if (farm.favorite) {
                        bookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_fill));
                    } else {
                        bookmark.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark));
                    }
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        dao.update(farm);
                    });
                });
                if (farm.favorite) {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(FarmProfileActivity.this, R.drawable.ic_bookmark_fill));
                } else {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(FarmProfileActivity.this, R.drawable.ic_bookmark));
                }
                title.setText(farm.name);
                birthCount.setText("" + farm.birthCount);
                showerCount.setText("" + farm.showerCount);
                if (farmWithNextVisit.nextVisit != null)
                    nextVisit.setText(farmWithNextVisit.nextVisit.toStringWithoutYear(this));
                else
                    nextVisit.setText(R.string.no_visit_short);
            });
            List<CowWithLastVisit> cows = dao.getAllCowOfFarmWithLastVisit(id);
            List<Cow> allCows = dao.getAllCowOfFarm(id);
            runOnUiThread(() -> {
                main:
                for (Cow cow : allCows) {
                    for (CowWithLastVisit cowWithLastVisit : cows) {
                        if (cow.getId().equals(cowWithLastVisit.getId())) {
                            continue main;
                        }
                    }
                    CowWithLastVisit temp = new CowWithLastVisit();
                    temp.setId(cow.getId());
                    temp.setLastVisit(null);
                    temp.setNumber(cow.getNumber());
                    cows.add(temp);
                }
                GridViewAdapterCowInFarmProfile adapter = new GridViewAdapterCowInFarmProfile(this, cows, id);
                cowsGridView.setAdapter(adapter);
            });
            List<NextVisit> list = dao.getAllNextVisitFroFarm(new MyDate(new Date()), id);
            runOnUiThread(() -> {
                if (list.isEmpty()) {
                    hideVisit();
                } else {
                    showVisit();
                    mAdapter.setNextVisits(list);
                    mAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void showVisit() {
        visitTitle.setVisibility(View.VISIBLE);
        nextVisitView.setVisibility(View.VISIBLE);
    }

    public void export() {

        if (Constants.checkPermission(this))
            return;

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sample sheet");

        Integer[] headers = {R.string.cow_number, R.string.day, R.string.month, R.string.year,
                R.string.injury_area, R.string.score_type, R.string.score, R.string.score_zero,
                R.string.score_one, R.string.score_two, R.string.cartie_state, R.string.drug_title_1,
                R.string.drug_title_2, R.string.drug_title_3, R.string.drug_title_4,
                R.string.drug_title_5, next_visit, more_info};
        Integer[] threeLevel = {R.string.score_three_one, R.string.score_three_two,
                R.string.score_three_three, R.string.score_three_four};
        Integer[] fourLevel = {R.string.score_four_one, R.string.score_four_two,
                R.string.score_four_three, R.string.score_four_four};
        Integer[] cartieState = {R.string.cartie_one, R.string.cartie_two,
                R.string.cartie_three, R.string.cartie_four};

        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<MyReport> reports = dao.getAllMyReportFarm(id);
            List<Drug> drugs = dao.getAllDrug();
            runOnUiThread(() -> {
                //add headers
                Row row = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(getString(headers[i]));
                }
                //add reports
                for (int i = 0; i < reports.size(); i++) {
                    MyReport myReport = reports.get(i);
                    Report report = myReport.report;
                    row = sheet.createRow(i + 1);

                    Cell cell = row.createCell(0);
                    cell.setCellValue(myReport.cowNumber);

                    int[] date = report.visit.convert(this);
                    cell = row.createCell(1);
                    cell.setCellValue(date[2]);

                    cell = row.createCell(2);
                    cell.setCellValue(date[1]);

                    cell = row.createCell(3);
                    cell.setCellValue(date[0]);

                    cell = row.createCell(4);
                    cell.setCellValue(report.areaNumber);

                    cell = row.createCell(5);
                    if (report.scoreType) {
                        cell.setCellValue(getString(R.string.three_level_text));
                    } else {
                        cell.setCellValue(getString(R.string.four_level_text));
                    }

                    cell = row.createCell(6);
                    if (report.scoreType) {
                        cell.setCellValue(getString(threeLevel[report.score]));
                    } else {
                        cell.setCellValue(getString(fourLevel[report.score]));
                    }


                    cell = row.createCell(7);
                    if (report.sardalme)
                        cell.setCellValue("*");

                    cell = row.createCell(8);
                    if (report.khoni)
                        cell.setCellValue("*");

                    cell = row.createCell(9);
                    if (report.kor)
                        cell.setCellValue("*");

                    cell = row.createCell(10);
                    if (report.cartieState != -1)
                        cell.setCellValue(getString(cartieState[report.cartieState]));

                    cell = row.createCell(11);
                    if (report.pomadeId != null)
                        if (report.pomadeId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.pomadeId) && drug.type.equals(0)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(12);
                    if (report.antibioticId != null)
                        if (report.antibioticId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.antibioticId) && drug.type.equals(1)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(13);
                    if (report.serumId != null)
                        if (report.serumId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.serumId) && drug.type.equals(2)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(14);
                    if (report.cureId != null)
                        if (report.cureId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.cureId) && drug.type.equals(3)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(15);
                    if (report.antiInflammatoryId != null)
                        if (report.antiInflammatoryId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.antiInflammatoryId) && drug.type.equals(4)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(16);
                    if (report.nextVisit != null)
                        cell.setCellValue(report.nextVisit.toString(this));

                    cell = row.createCell(17);
                    cell.setCellValue(report.description);

                }
            });
        });

        AppExecutors.getInstance().diskIO().execute(() -> {
            Farm farm = dao.getFarm(id);
            runOnUiThread(() -> {
                try {

                    String storage = Environment.getExternalStorageDirectory().toString() + String.format("/%s.xls", farm.name);
                    File file = new File(storage);
                    if (file.exists()) {
                        if (file.delete()) {
                            Log.i("TAG", "export: deleted ok");
                        } else {
                            Log.i("TAG", "export: deleted fuck");
                        }
                    }
                    if (file.createNewFile()) {
                        FileOutputStream out = new FileOutputStream(file);
                        workbook.write(out);
                        out.close();
                        Log.i("TAG", "export: Excel written successfully..");
                    } else {
                        Log.i("TAG", "export: Excel written fuck..");
                    }

                    Uri uri;
                    if (Build.VERSION.SDK_INT < 24) {
                        uri = Uri.fromFile(file);
                    } else {
                        uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                    }

                    Intent intent = ShareCompat.IntentBuilder.from(this)
                            .setType("*/*")
                            .setStream(uri)
                            .setChooserTitle("Choose bar")
                            .createChooserIntent()
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(intent);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        });


    }

}