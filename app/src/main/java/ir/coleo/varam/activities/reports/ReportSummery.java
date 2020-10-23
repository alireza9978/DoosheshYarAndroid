package ir.coleo.varam.activities.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import ir.coleo.varam.R;
import ir.coleo.varam.adapters.GridViewAdapterItemInSummery;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.MyReport;
import ir.coleo.varam.database.models.Report;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.DateContainer;
import ir.coleo.varam.ui_element.ExpandableHeightGridView;

import java.util.ArrayList;
import java.util.Objects;

public class ReportSummery extends AppCompatActivity {

    private ImageView outside;
    private ImageView menu;
    private ConstraintLayout menuLayout;
    private TextView cowText;
    private ExpandableHeightGridView reasonsGrid;
    private ExpandableHeightGridView moreInfoGrid;
    private TextView area;
    private TextView lastVisit;
    private TextView description;
    private GridViewAdapterItemInSummery reasonAdapter;
    private GridViewAdapterItemInSummery moreInfoAdapter;
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
        reasonsGrid = findViewById(R.id.reason_grid);
        moreInfoGrid = findViewById(R.id.more_info_grid);
        area = findViewById(R.id.area_text);
        lastVisit = findViewById(R.id.last_visit_text);
        description = findViewById(R.id.description_text);

        reasonAdapter = new GridViewAdapterItemInSummery(this, new ArrayList<>());
        moreInfoAdapter = new GridViewAdapterItemInSummery(this, new ArrayList<>());

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
            Report report = myReport.report;
            runOnUiThread(() -> {
                cowText.setText(getString(R.string.report));
                cowText.append(" " + myReport.report.id);
                cowText.append(getString(R.string.cow_title));
                cowText.append("" + myReport.cowNumber);

                area.setText(getString(R.string.injury_area) + " " + report.legAreaNumber +
                        ", " + getString(R.string.finger) + " " + report.fingerNumber);
                DateContainer container;
                if (Constants.getDefualtlanguage(this).equals("fa")) {
                    int[] temp = report.visit.convert(this);
                    DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                } else {
                    DateContainer.MyDate date = new DateContainer.MyDate(false, report.visit.getYear(), report.visit.getMonth(), report.visit.getDay());
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                }
                lastVisit.setText(container.toString(this));
                description.setText(report.description);

                reasonAdapter.setItems(report.getSelectedReason());
                reasonsGrid.setAdapter(reasonAdapter);
                reasonAdapter.notifyDataSetChanged();

                moreInfoAdapter.setItems(report.getSelectedOtherInfo());
                moreInfoGrid.setAdapter(moreInfoAdapter);
                moreInfoAdapter.notifyDataSetChanged();
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