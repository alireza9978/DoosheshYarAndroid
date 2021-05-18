package ir.coleo.varam.activities.tabs.home_activites;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import ir.coleo.varam.R;
import ir.coleo.varam.adapters.RecyclerViewAdapterAllNextVisit;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.NextReport;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.MyDate;

/**
 * صفحه لیست بازدید های بعدی در خانه
 */
public class VisitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());
        Constants.setImageBack(this, back);

        RecyclerView listView = findViewById(R.id.all_visit_lists);

        MyDao dao = DataBase.getInstance(this).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<NextReport> reports = dao.getAllNextVisit(new MyDate(new Date()));
            RecyclerViewAdapterAllNextVisit mAdapter = new RecyclerViewAdapterAllNextVisit(reports, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            listView.setLayoutManager(layoutManager);
            listView.setAdapter(mAdapter);
        });

    }
}