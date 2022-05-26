package ir.coleo.varam.activities;

import static android.os.Build.VERSION.SDK_INT;
import static ir.coleo.varam.R.string.more_info;
import static ir.coleo.varam.R.string.next_visit;
import static ir.coleo.varam.R.string.score;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_EXPORT_REPORT;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_RESULT;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gun0912.tedpermission.PermissionListener;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.tabs.AddFarmActivity;
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
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.dialog.DateModelDialog;
import ir.coleo.varam.dialog.SureDialog;
import ir.coleo.varam.models.DateContainer;
import ir.coleo.varam.models.MyDate;


/**
 * صفحه پروفایل گاوداری
 * گرفتن خروجی اکسل برای گاوداری
 * حذف گاوداری
 * افزدن گاو
 * ویراشی گاوداری
 */
public class FarmProfileActivity extends AppCompatActivity {

    private TextView title;
    private TextView visitTitle;
    private TextView showerCount;
    private TextView bedType;
    private TextView scoreMethodTextView;
    private TextView birthCount;
    private TextView nextVisit;
    private ImageView bookmark;
    private RecyclerView cowsGridView;
    private RecyclerView nextVisitView;
    private RecyclerViewAdapterNextVisitFarmProfile mAdapter;
    private ImageView menu;
    private ConstraintLayout menuLayout;
    private ImageView outside;
    private int id;
    private DateContainer dateContainerOne;
    private PermissionListener permissionlistener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock_profile);

        menu = findViewById(R.id.dropdown_menu);
        menuLayout = findViewById(R.id.menu_layout);
        outside = findViewById(R.id.outside);
        bedType = findViewById(R.id.bed_type_value);
        scoreMethodTextView = findViewById(R.id.score_type_value);
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

        RecyclerView.LayoutManager cowLayoutManager = new GridLayoutManager(this, 2);
        cowsGridView.setLayoutManager(cowLayoutManager);
        mAdapter = new RecyclerViewAdapterNextVisitFarmProfile(new ArrayList<>(), this);
        nextVisitView.setAdapter(mAdapter);

        menu.setOnClickListener(view -> showMenu());
        outside.setOnClickListener(view -> hideMenu());
        ConstraintLayout edit = findViewById(R.id.item_one);
        ConstraintLayout remove = findViewById(R.id.item_two);
        ConstraintLayout share = findViewById(R.id.item_three);
        edit.setOnClickListener(view -> {
            hideMenu();
            Intent intent = new Intent(this, AddFarmActivity.class);
            intent.putExtra(Constants.FARM_ID, id);
            intent.putExtra(Constants.ADD_FARM_MODE, Constants.EDIT_FARM);
            startActivity(intent);
        });
        remove.setOnClickListener(view -> {
            MyDao dao = DataBase.getInstance(this).dao();
            SureDialog dialog = new SureDialog(FarmProfileActivity.this, getString(R.string.delete_question),
                    getString(R.string.delete),
                    () -> AppExecutors.getInstance().diskIO().execute(() -> {
                        runOnUiThread(() -> Toast.makeText(FarmProfileActivity.this, "در حال حذف، اندکی صبر کنید.", Toast.LENGTH_SHORT).show());
                        Farm farm = dao.getFarm(id);
                        dao.deleteFarm(farm);
                        runOnUiThread(() -> {
                            hideMenu();
                            finish();
                        });
                    }),
                    () -> runOnUiThread(this::hideMenu), getString(R.string.yes),
                    getString(R.string.no));
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
        });
        share.setOnClickListener(view -> {
            checkExportPermission();
            hideMenu();
        });

        permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(FarmProfileActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                dateContainerOne = null;
                selectDate();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(FarmProfileActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

    }

    private void selectDate() {
        DateModelDialog dialog = new DateModelDialog(this);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public void getDate(boolean single) {
        Intent intent = new Intent(this, DateSelectionActivity.class);
        if (single) {
            intent.setAction(Constants.DateSelectionMode.SINGLE);
        } else {
            intent.setAction(Constants.DateSelectionMode.RANG);
        }
        startActivityForResult(intent, DATE_SELECTION_EXPORT_REPORT);
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
            ScoreMethod scoreMethod = dao.getScoreMethod(farm.scoreMethodId);
            runOnUiThread(() -> {
                bedType.setText(farm.bedType);
                scoreMethodTextView.setText(scoreMethod.getText());
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
                // Changes the height and width to the specified *pixels*
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                cowsGridView.getLayoutParams().height = 5000;
                cowsGridView.requestLayout();
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

    public void checkExportPermission() {
        Constants.checkPermission(permissionlistener);
    }

    public void export() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sample sheet");

        Integer[] headers = {R.string.cow_number, R.string.day, R.string.month, R.string.year,
                R.string.cartie_number_one, R.string.cartie_number_two, R.string.cartie_number_three,
                R.string.cartie_number_four, R.string.option_five, R.string.option_six, R.string.option_two,
                R.string.option_three, R.string.option_eight, R.string.option_four,
                R.string.option_one, R.string.option_seven_xlsx, R.string.drug_title_1,
                R.string.drug_title_2, R.string.drug_title_3, R.string.drug_title_4,
                R.string.drug_title_5, next_visit, more_info, R.string.cure_duration, R.string.chronic, R.string.recurrence};

        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {

            List<MyReport> reports;
            if (dateContainerOne == null) {
                reports = dao.getAllMyReportFarm(id);
            } else {
                if (dateContainerOne.getEndDate() != null) {
                    reports = dao.getAllMyReportFarm((long) id, dateContainerOne.exportStart(), dateContainerOne.exportEnd());
                } else {
                    reports = dao.getAllMyReportFarm((long) id, dateContainerOne.exportStart());
                }
            }

            Farm farm = dao.getFarm(id);
            ScoreMethod scoreMethod = dao.getScoreMethod(farm.scoreMethodId);
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
                    cell.setCellValue(String.format("%02d", date[2]));

                    cell = row.createCell(2);
                    cell.setCellValue(String.format("%02d", date[1]));

                    cell = row.createCell(3);
                    cell.setCellValue(String.format("%02d", date[0]));

                    if (report.areaNumber != null) {
                        for (int j = 4; j < 8; j++) {
                            cell = row.createCell(j);
                            if (report.score != null) {
                                if (j == 4 + (report.areaNumber - 1)) {
                                    cell.setCellValue(scoreMethod.scoresNameList.get(report.score));
                                    Log.e("TAG", "export: "+report.score + scoreMethod.scoresNameList.get(report.score));
                                }
                            } else {
                                if (j == 4 + (report.areaNumber - 1)) {
                                    cell.setCellValue("*");
                                }
                            }
                        }
                    }

                    cell = row.createCell(8);
                    if (report.sardalme)
                        cell.setCellValue("*");

                    cell = row.createCell(9);
                    if (report.khoni)
                        cell.setCellValue("*");

                    for (int j = 10; j < 15; j++) {
                        cell = row.createCell(j);
                        if (report.cartieState != null)
                            if (j == 10 + report.cartieState) {
                                cell.setCellValue("*");
                            }
                    }


                    cell = row.createCell(15);
                    if (report.kor)
                        cell.setCellValue("*");

                    cell = row.createCell(16);
                    if (report.pomadeId != null)
                        if (report.pomadeId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.pomadeId) && drug.type.equals(0)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(17);
                    if (report.antibioticId != null)
                        if (report.antibioticId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.antibioticId) && drug.type.equals(1)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(18);
                    if (report.serumId != null)
                        if (report.serumId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.serumId) && drug.type.equals(2)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(19);
                    if (report.cureId != null)
                        if (report.cureId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.cureId) && drug.type.equals(3)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(20);
                    if (report.antiInflammatoryId != null)
                        if (report.antiInflammatoryId >= 0) {
                            for (Drug drug : drugs) {
                                if (drug.id.equals(report.antiInflammatoryId) && drug.type.equals(4)) {
                                    cell.setCellValue(drug.name);
                                    break;
                                }
                            }
                        }

                    cell = row.createCell(21);
                    if (report.nextVisit != null)
                        cell.setCellValue(report.nextVisit.toString(this));

                    cell = row.createCell(22);
                    cell.setCellValue(report.description);

                    cell = row.createCell(23);
                    cell.setCellValue(report.cureDuration);

                    cell = row.createCell(24);
                    if (report.chronic)
                        cell.setCellValue("*");

                    cell = row.createCell(25);
                    if (report.recurrence)
                        cell.setCellValue("*");
                }
            });
        });

        AppExecutors.getInstance().diskIO().execute(() -> {
            Farm farm = dao.getFarm(id);
            runOnUiThread(() -> {
                try {
                    String storage = Environment.getExternalStorageDirectory().toString() + String.format("/%s.xlsx", farm.name);
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
                    if (SDK_INT < 24) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DATE_SELECTION_EXPORT_REPORT) {
            if (resultCode == Constants.DATE_SELECTION_OK) {
                assert data != null;
                DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(DATE_SELECTION_RESULT);
                assert container != null;
                dateContainerOne = container;
                export();
            }
        }
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    export();
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}