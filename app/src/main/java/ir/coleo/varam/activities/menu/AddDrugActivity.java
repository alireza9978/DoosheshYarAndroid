package ir.coleo.varam.activities.menu;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.adapters.RecyclerViewAdapterDrugList;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Drug;
import ir.coleo.varam.database.utils.AppExecutors;

/**
 * صفحه افزودن یکی از پنج مورد از دارو به لیست دارو‌ها
 */
public class AddDrugActivity extends AppCompatActivity {

    private RecyclerViewAdapterDrugList adapterDrugList;
    private MyDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);
        EditText drugName = findViewById(R.id.enter_drug_name);

        int type = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.DRUG_TYPE, -1);
        if (type == -1) {
            return;
        } else {
            TextView textView = findViewById(R.id.page_title);
            switch (type) {
                case 0: {
                    textView.setText(R.string.drug_title_1);
                    drugName.setHint(R.string.enter_drug_1_name);
                    break;
                }
                case 1: {
                    textView.setText(R.string.drug_title_2);
                    drugName.setHint(R.string.enter_drug_2_name);
                    break;
                }
                case 2: {
                    textView.setText(R.string.drug_title_3);
                    drugName.setHint(R.string.enter_drug_3_name);
                    break;
                }
                case 3: {
                    textView.setText(R.string.drug_title_4);
                    drugName.setHint(R.string.enter_drug_4_name);
                    break;
                }
                case 4: {
                    textView.setText(R.string.drug_title_5);
                    drugName.setHint(R.string.enter_drug_5_name);
                    break;
                }

            }
        }

        findViewById(R.id.close_image).setOnClickListener(view -> finish());
        dao = DataBase.getInstance(this).dao();
        findViewById(R.id.create_drug).setOnClickListener(view -> {
            if (drugName.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.check_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            AppExecutors.getInstance().diskIO().execute(() -> {
                Drug drug = new Drug();
                drug.type = type;
                drug.name = drugName.getText().toString();
                dao.insert(drug);
                runOnUiThread(() -> {
                    drugName.setText("");
                    Toast.makeText(this, R.string.added, Toast.LENGTH_SHORT).show();
                    updateList(type);
                });
            });
        });


        RecyclerView recyclerView = findViewById(R.id.drugs_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapterDrugList = new RecyclerViewAdapterDrugList(this);
        recyclerView.setAdapter(adapterDrugList);
        updateList(type);

    }

    public void delete(Drug drug) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            dao.deleteDrug(drug);
            updateList(drug.type);
        });
    }

    public void updateList(int type) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Drug> drugs = dao.getAllDrug(type);
            runOnUiThread(() -> {
                adapterDrugList.setDrugs(drugs);
                adapterDrugList.notifyDataSetChanged();
            });
        });
    }

}
