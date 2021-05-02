package ir.coleo.varam.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import ir.coleo.varam.R;
import ir.coleo.varam.adapters.RecyclerViewAdapterDrugSimple;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Drug;
import ir.coleo.varam.database.utils.AppExecutors;

public class DrugSelectionActivity extends AppCompatActivity {

    private Context context = this;
    private RecyclerView recyclerView;
    private int drugType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_selection);

        drugType = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.DRUG_TYPE, -1);
        if (drugType == -1)
            return;
        TextView textView = findViewById(R.id.page_title);
        String drugName = "";
        switch (drugType) {
            case 0: {
                drugName = getString(R.string.drug_title_1);
                break;
            }
            case 1: {
                drugName = getString(R.string.drug_title_2);
                break;
            }
            case 2: {
                drugName = getString(R.string.drug_title_3);
                break;
            }
            case 3: {
                drugName = getString(R.string.drug_title_4);
                break;
            }
            case 4: {
                drugName = getString(R.string.drug_title_5);
                break;
            }
        }
        String title;
        if (Constants.getDefaultLanguage(this).equals("fa")) {
            title = "لیست " + drugName;
        } else {
            title = drugName + " lists";
        }
        textView.setText(title);

        findViewById(R.id.close_image).setOnClickListener(view -> finish());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.livestroks_lists);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = findViewById(R.id.page_title);
        String drugName = "";
        switch (drugType) {
            case 0: {
                drugName = getString(R.string.drug_title_1);
                break;
            }
            case 1: {
                drugName = getString(R.string.drug_title_2);
                break;
            }
            case 2: {
                drugName = getString(R.string.drug_title_3);
                break;
            }
            case 3: {
                drugName = getString(R.string.drug_title_4);
                break;
            }
            case 4: {
                drugName = getString(R.string.drug_title_5);
                break;
            }
        }
        String title;
        if (Constants.getDefaultLanguage(this).equals("fa")) {
            title = "لیست " + drugName;
        } else {
            title = drugName + " lists";
        }
        textView.setText(title);

        MyDao dao = DataBase.getInstance(this).dao();
        AtomicReference<RecyclerViewAdapterDrugSimple> adapter = new AtomicReference<>();
        String finalDrugName = drugName;
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Drug> drugs = dao.getAllDrug(drugType);
            runOnUiThread(() -> {
                if (drugs.isEmpty()) {
                    String message;
                    if (Constants.getDefaultLanguage(this).equals("fa")) {
                        message = "هیچ " + finalDrugName + " ‌ای" + " پیدا نشد";
                    } else {
                        message = "no " + finalDrugName + " available";
                    }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
                adapter.set(new RecyclerViewAdapterDrugSimple(drugs, context, drugType));
                recyclerView.setAdapter(adapter.get());
            });

        });
    }

    public void selectedDrug(int id) {
        Intent intent = new Intent();
        intent.putExtra(Constants.DRUG_ID, id);
        intent.putExtra(Constants.DRUG_TYPE, drugType);
        setResult(Constants.DATE_SELECTION_OK, intent);
        finish();
    }

}