package ir.coleo.varam.activities.tabs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.Farm;
import ir.coleo.varam.database.utils.AppExecutors;

import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

public class AddLivestockActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_livestock);

        String mode = (String) Objects.requireNonNull(getIntent().getExtras()).get(Constants.ADD_FARM_MODE);
        if (mode == null) {
            finish();
        } else {
            findViewById(R.id.close_image).setOnClickListener((View v) -> finish());
            EditText farmTitle = findViewById(R.id.farm_title_input);
            EditText controlSystem = findViewById(R.id.control_system_input);
            EditText birth = findViewById(R.id.something_count);
            Button submit = findViewById(R.id.submit);
            if (mode.equals(Constants.FARM_CREATE)) {
                submit.setOnClickListener((View v) -> {
                    String title = farmTitle.getText().toString();
                    String system = controlSystem.getText().toString();
                    String countString = birth.getText().toString();
                    if (title.isEmpty() | system.isEmpty() | countString.isEmpty()) {
                        Toast.makeText(this, R.string.check_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int count = Integer.parseInt(countString);
                    Farm farm = new Farm();
                    farm.favorite = false;
                    farm.controlSystem = system;
                    farm.birthCount = count;
                    farm.name = title;
                    MyDao dao = DataBase.getInstance(this).dao();
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        dao.insert(farm);
                        finish();
                    });
                });
            }
            if (mode.equals(Constants.EDIT_FARM)) {
                int id = getIntent().getExtras().getInt(Constants.FARM_ID);
                submit.setText(getString(R.string.edit));
                MyDao dao = DataBase.getInstance(this).dao();
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Farm farm = dao.getFarm(id);
                    runOnUiThread(() -> {
                        farmTitle.setText(farm.name);
                        farmTitle.setEnabled(false);
                        controlSystem.setText(farm.controlSystem);
                        birth.setText("" + farm.birthCount);
                    });
                });

                submit.setOnClickListener((View v) -> {
                    String title = farmTitle.getText().toString();
                    String system = controlSystem.getText().toString();
                    String countString = birth.getText().toString();
                    if (title.isEmpty() | system.isEmpty() | countString.isEmpty()) {
                        Toast.makeText(this, R.string.check_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int count = Integer.parseInt(countString);
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        Farm farm = dao.getFarm(id);
                        farm.controlSystem = system;
                        farm.birthCount = count;
                        dao.update(farm);
                        runOnUiThread(() -> finish());
                    });
                });
                controlSystem.requestFocus();
            }

        }


    }
}