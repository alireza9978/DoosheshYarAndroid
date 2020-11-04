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

import ir.coleo.varam.R;
import ir.coleo.varam.activities.tabs.AddLivestockActivity;
import ir.coleo.varam.adapters.GridViewAdapterCowInFarmProfile;
import ir.coleo.varam.adapters.RecyclerViewAdapterNextVisitFarmProfile;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Cow;
import ir.coleo.varam.database.models.CowWithLastVisit;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.models.FarmWithNextVisit;
import ir.coleo.varam.database.models.MyReport;
import ir.coleo.varam.database.models.NextVisit;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.MyDate;

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

import static ir.coleo.varam.R.string.eight;
import static ir.coleo.varam.R.string.eleven;
import static ir.coleo.varam.R.string.five;
import static ir.coleo.varam.R.string.four;
import static ir.coleo.varam.R.string.more_info;
import static ir.coleo.varam.R.string.more_info_reason_1;
import static ir.coleo.varam.R.string.more_info_reason_2;
import static ir.coleo.varam.R.string.more_info_reason_3;
import static ir.coleo.varam.R.string.more_info_reason_4;
import static ir.coleo.varam.R.string.more_info_reason_5;
import static ir.coleo.varam.R.string.more_info_reason_6;
import static ir.coleo.varam.R.string.more_info_reason_7;
import static ir.coleo.varam.R.string.next_visit;
import static ir.coleo.varam.R.string.nine;
import static ir.coleo.varam.R.string.one;
import static ir.coleo.varam.R.string.reason_1;
import static ir.coleo.varam.R.string.reason_10;
import static ir.coleo.varam.R.string.reason_2;
import static ir.coleo.varam.R.string.reason_3;
import static ir.coleo.varam.R.string.reason_4;
import static ir.coleo.varam.R.string.reason_5;
import static ir.coleo.varam.R.string.reason_6;
import static ir.coleo.varam.R.string.reason_7;
import static ir.coleo.varam.R.string.reason_8;
import static ir.coleo.varam.R.string.reason_9;
import static ir.coleo.varam.R.string.seven;
import static ir.coleo.varam.R.string.six;
import static ir.coleo.varam.R.string.ten;
import static ir.coleo.varam.R.string.three;
import static ir.coleo.varam.R.string.twelve;
import static ir.coleo.varam.R.string.two;
import static ir.coleo.varam.R.string.zero;

public class FarmProfileActivity extends AppCompatActivity {

    private TextView title;
    private TextView visitTitle;
    private TextView showerCount;
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

//        if (Constants.checkPermission(this))
//            return;
//
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Sample sheet");
//
//        Integer[] headers = {R.string.cow_number, R.string.day, R.string.month, R.string.year,
//                reason_1, reason_2, reason_3,
//                reason_6, reason_7, reason_9, reason_8, reason_4,
//                reason_5, reason_10, zero, one, two, three, four, five, six, seven, eight, nine,
//                ten, eleven, twelve, more_info_reason_1, more_info_reason_2, more_info_reason_7,
//                more_info_reason_5, more_info_reason_6, more_info_reason_4, more_info_reason_3,
//                next_visit, more_info};
//
//        MyDao dao = DataBase.getInstance(this).dao();
//        AppExecutors.getInstance().diskIO().execute(() -> {
//            List<MyReport> reports = dao.getAllMyReportFarm(id);
//            runOnUiThread(() -> {
//                //add headers
//                Row row = sheet.createRow(0);
//                for (int i = 0; i < headers.length; i++) {
//                    Cell cell = row.createCell(i);
//                    cell.setCellValue(getString(headers[i]));
//                }
//                //add reports
//                for (int i = 0; i < reports.size(); i++) {
//                    MyReport myReport = reports.get(i);
//                    Report report = myReport.report;
//                    row = sheet.createRow(i + 1);
//
//                    Cell cell = row.createCell(0);
//                    cell.setCellValue(myReport.cowNumber);
//
//                    int[] date = report.visit.convert(this);
//                    cell = row.createCell(1);
//                    cell.setCellValue(date[2]);
//
//                    cell = row.createCell(2);
//                    cell.setCellValue(date[1]);
//
//                    cell = row.createCell(3);
//                    cell.setCellValue(date[0]);
//
//                    cell = row.createCell(4);
//                    if (report.referenceCauseHundredDays)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(5);
//                    if (report.referenceCauseDryness)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(6);
//                    if (report.referenceCauseLagged)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(7);
//                    if (report.referenceCauseHighScore)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(8);
//                    if (report.referenceCauseReferential)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(9);
//                    if (report.referenceCauseHeifer)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(10);
//                    if (report.referenceCauseLongHoof)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(11);
//                    if (report.referenceCauseNewLimp)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(12);
//                    if (report.referenceCauseLimpVisit)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(13);
//                    if (report.referenceCauseGroupHoofTrim)
//                        cell.setCellValue("*");
//
//                    for (int k = 0; k < 13; k++) {
//                        cell = row.createCell(k + 14);
//                        if (report.legAreaNumber == k)
//                            cell.setCellValue(report.fingerNumber);
//                    }
//
//                    cell = row.createCell(27);
//                    if (report.otherInfoWound)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(28);
//                    if (report.otherInfoEcchymosis)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(29);
//                    if (report.otherInfoHoofTrim)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(30);
//                    if (report.otherInfoGel)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(31);
//                    if (report.otherInfoBoarding)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(32);
//                    if (report.otherInfoNoInjury)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(33);
//                    if (report.otherInfoRecovered)
//                        cell.setCellValue("*");
//
//                    cell = row.createCell(34);
//                    if (report.nextVisit != null)
//                        cell.setCellValue(report.nextVisit.toString(this));
//
//                    cell = row.createCell(35);
//                    cell.setCellValue(report.description);
//
//                }
//            });
//        });
//
//        AppExecutors.getInstance().diskIO().execute(() -> {
//            Farm farm = dao.getFarm(id);
//            runOnUiThread(() -> {
//                try {
//
//                    String storage = Environment.getExternalStorageDirectory().toString() + String.format("/%s.xls", farm.name);
//                    File file = new File(storage);
//                    if (file.exists()) {
//                        if (file.delete()) {
//                            Log.i("TAG", "export: deleted ok");
//                        } else {
//                            Log.i("TAG", "export: deleted fuck");
//                        }
//                    }
//                    if (file.createNewFile()) {
//                        FileOutputStream out = new FileOutputStream(file);
//                        workbook.write(out);
//                        out.close();
//                        Log.i("TAG", "export: Excel written successfully..");
//                    } else {
//                        Log.i("TAG", "export: Excel written fuck..");
//                    }
//
//                    Uri uri;
//                    if (Build.VERSION.SDK_INT < 24) {
//                        uri = Uri.fromFile(file);
//                    } else {
//                        uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
//                    }
//
//                    Intent intent = ShareCompat.IntentBuilder.from(this)
//                            .setType("*/*")
//                            .setStream(uri)
//                            .setChooserTitle("Choose bar")
//                            .createChooserIntent()
//                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                    startActivity(intent);
//
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            });
//        });


    }

}