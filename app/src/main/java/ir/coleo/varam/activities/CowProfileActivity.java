package ir.coleo.varam.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import ir.coleo.varam.R;
import ir.coleo.varam.adapters.GridViewAdapterCowProfile;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Cow;
import ir.coleo.varam.database.models.LastReport;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.utils.AppExecutors;

import java.util.List;
import java.util.Objects;

public class CowProfileActivity extends AppCompatActivity {

    private TextView title;
    private TextView lastVisit;
    private TextView nextVisit;
    private ImageView bookmark;
    private ImageView exit;
    private ImageView menu;
    private GridView reports;
    private Context context;
    private Cow cow;
    private GridViewAdapterCowProfile adapter;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cattel_profile);
        context = this;
        title = findViewById(R.id.title_livestrok);
        bookmark = findViewById(R.id.bookmark_image);
        lastVisit = findViewById(R.id.count_value);
        nextVisit = findViewById(R.id.system_value);
        menu = findViewById(R.id.dropdown_menu);
        reports = findViewById(R.id.reports_list);
        exit = findViewById(R.id.back_icon);
        exit.setOnClickListener(view -> finish());
        Constants.setImageBackBorder(this, exit);

        id = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.COW_ID);
        exit.setOnClickListener((v) -> finish());


    }

    @Override
    protected void onResume() {
        super.onResume();
        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            cow = dao.getCow(id);
            List<Report> reports = dao.getAllReportOfCow(cow.getId());
            runOnUiThread(() -> {
                title.setText(cow.getNumber(context));
                if (cow.getFavorite()) {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_fill));
                } else {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark));
                }
                adapter = new GridViewAdapterCowProfile(this, reports, cow.getId());
                this.reports.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
            LastReport lastVisit = dao.getLastReport(cow.getId());

            runOnUiThread(() -> {
                if (lastVisit.nextVisit != null) {
                    this.nextVisit.setText(lastVisit.nextVisit.toString(context));
                } else {
                    this.nextVisit.setText(R.string.no_visit_short);
                }
                if (lastVisit.lastVisit != null) {
                    this.lastVisit.setText(lastVisit.lastVisit.toString(context));
                } else {
                    this.lastVisit.setText(R.string.no_visit_short);
                }
            });
        });
        bookmark.setOnClickListener(view -> {
            if (cow != null) {
                cow.setFavorite(!cow.getFavorite());
                AppExecutors.getInstance().diskIO().execute(() -> {
                    dao.update(cow);
                });
                if (cow.getFavorite()) {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_fill));
                } else {
                    bookmark.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bookmark));
                }
            }
        });
    }
}