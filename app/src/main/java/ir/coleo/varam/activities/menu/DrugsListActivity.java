package ir.coleo.varam.activities.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;

/**
 * صفحه انتخاب نوع دارو برای افزودن
 */
public class DrugsListActivity extends AppCompatActivity {

    private int[] drugsId = new int[]{R.id.drug_text_one, R.id.drug_text_two,
            R.id.drug_text_three, R.id.drug_text_four, R.id.drug_text_five};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drugs_list);
        ImageView exit = findViewById(R.id.back_icon);
        exit.setOnClickListener(view -> finish());
        Constants.setImageBackBorder(this, exit);

        for (int i = 0; i < drugsId.length; i++) {
            int finalI = i;

            findViewById(drugsId[i]).setOnClickListener(view1 -> {
                Intent intent = new Intent(this, AddDrugActivity.class);
                intent.putExtra(Constants.DRUG_TYPE, finalI);
                startActivity(intent);
            });
        }

    }
}