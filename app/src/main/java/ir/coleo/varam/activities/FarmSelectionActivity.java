package ir.coleo.varam.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ir.coleo.varam.R;
import ir.coleo.varam.adapters.RecyclerViewAdapterFarmSimple;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.utils.AppExecutors;

/**
 * صفحه انتخابگر از بین گاوداری های موجود
 * استفاده شده در بخش های گزارش گیری
 */
public class FarmSelectionActivity extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_strock_selection);

        findViewById(R.id.close_image).setOnClickListener(view -> {
            finish();
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.livestroks_lists);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        MyDao dao = DataBase.getInstance(this).dao();
        AtomicReference<RecyclerViewAdapterFarmSimple> adapter = new AtomicReference<>();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Farm> farmList = dao.getAll();
            runOnUiThread(() -> {
                adapter.set(new RecyclerViewAdapterFarmSimple(farmList, context));
                recyclerView.setAdapter(adapter.get());
            });

        });


    }

    public void selectedFarm(int id) {
        Intent intent = new Intent();
        intent.putExtra(Constants.FARM_ID, id);
        setResult(Constants.DATE_SELECTION_OK, intent);
        finish();
    }

}