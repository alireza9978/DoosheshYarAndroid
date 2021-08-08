package ir.coleo.varam.activities.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.adapters.GridViewAdapterItemInSummery;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.MyReport;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.DateContainer;
import ir.coleo.varam.ui_element.ExpandableHeightGridView;

/**
 * صفحه بررسی گزارش ثبت شده
 */
public class ReportSummery extends AppCompatActivity {

    private ImageView outside;
    private ImageView menu;
    private ConstraintLayout menuLayout;
    private TextView cowText;
    private ExpandableHeightGridView drugsGrid;
    private TextView lastVisit;
    private TextView description;
    private TextView cartieNumber;
    private TextView cartieState;
    private GridViewAdapterItemInSummery drugAdapter;
    private Integer reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_summery);

        reportId = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.REPORT_ID);

        ImageView back = findViewById(R.id.back_icon);
        Constants.setImageBackBorder(this, back);
        menuLayout = findViewById(R.id.menu_layout);
        outside = findViewById(R.id.outside);
        menu = findViewById(R.id.dropdown_menu);
        cowText = findViewById(R.id.cow_title);
        lastVisit = findViewById(R.id.last_visit_text);
        description = findViewById(R.id.description_text);
        cartieNumber = findViewById(R.id.cartie_number_text);
        cartieState = findViewById(R.id.cartie_state_text);
        drugsGrid = findViewById(R.id.drug_list);

        drugAdapter = new GridViewAdapterItemInSummery(this, new ArrayList<>());

        MyDao dao = DataBase.getInstance(this).dao();

        back.setOnClickListener(view -> finish());
        menu.setOnClickListener(view -> showMenu());
        outside.setOnClickListener(view -> hideMenu());
        ConstraintLayout edit = findViewById(R.id.item_one);
        ConstraintLayout remove = findViewById(R.id.item_two);
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddReportActivity.class);
            intent.putExtra(Constants.REPORT_MODE, Constants.EDIT_REPORT);
            intent.putExtra(Constants.REPORT_ID, reportId);
            startActivity(intent);
            hideMenu();
        });
        remove.setOnClickListener(view -> AppExecutors.getInstance().diskIO().execute(() -> {
            Report report = dao.getReport(reportId);
            dao.deleteReport(report);
            runOnUiThread(() -> {
                hideMenu();
                finish();
            });
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            MyReport myReport = dao.myReportWithCow(reportId);
            ArrayList<String> drugs = new ArrayList<>();
            Report report = myReport.report;
            if (report.pomadeId != null)
                drugs.add(dao.getDrug(report.pomadeId).name);
            if (report.serumId != null)
                drugs.add(dao.getDrug(report.serumId).name);
            if (report.antiInflammatoryId != null)
                drugs.add(dao.getDrug(report.antiInflammatoryId).name);
            if (report.antibioticId != null)
                drugs.add(dao.getDrug(report.antibioticId).name);
            if (report.cureId != null)
                drugs.add(dao.getDrug(report.cureId).name);


            runOnUiThread(() -> {
                cowText.setText(getString(R.string.report));
                cowText.append(" " + myReport.report.id);
                cowText.append(getString(R.string.cow_title));
                cowText.append("" + myReport.cowNumber);

                if (report.areaNumber != null) {
                    cartieNumber.setText(R.string.area);
                    cartieNumber.append(" " + report.areaNumber);
                }
                if (report.cartieState == null) {
                    report.cartieState = -1;
                }
                switch (report.cartieState) {
                    case 0: {
                        cartieState.setText(R.string.option_one);
                        break;
                    }
                    case 1: {
                        cartieState.setText(R.string.option_two);
                        break;
                    }
                    case 2: {
                        cartieState.setText(R.string.option_three);
                        break;
                    }
                    case 3: {
                        cartieState.setText(R.string.option_four);
                        break;
                    }
                    default:
                        cartieState.setText(R.string.unknown);
                }

                DateContainer container;
                if (Constants.getDefaultLanguage(this).equals("fa")) {
                    int[] temp = report.visit.convert(this);
                    DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                } else {
                    DateContainer.MyDate date = new DateContainer.MyDate(false, report.visit.getYear(), report.visit.getMonth(), report.visit.getDay());
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                }
                lastVisit.setText(container.toString(this));
                description.setText(report.description);

                drugAdapter.setItems(drugs);
                drugsGrid.setAdapter(drugAdapter);
                drugAdapter.notifyDataSetChanged();
            });
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


}