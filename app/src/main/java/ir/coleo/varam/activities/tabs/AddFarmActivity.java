package ir.coleo.varam.activities.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.database.utils.AppExecutors;

/**
 * صفحه ایجاد یک گاوداری جدید
 */
public class AddFarmActivity extends AppCompatActivity {

    //    private Boolean dryMethod = null;
    private ScoreMethod scoreMethod = null;


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
            EditText birthCount = findViewById(R.id.birth_count);
            EditText showerCount = findViewById(R.id.shower_count);
            EditText bedType = findViewById(R.id.bed_count);
            EditText showerUnitCount = findViewById(R.id.shower_unit_count);
            EditText showerPitCount = findViewById(R.id.shower_pit_count);

            farmTitle.requestFocus();

            Button submit = findViewById(R.id.submit);
            ActivityResultLauncher<Intent> three = makeCheckBoxList(R.id.three_check, R.id.three_text, new ArrayList<>(Arrays.asList(R.id.four_check,R.id.five_check)));
            ActivityResultLauncher<Intent> four = makeCheckBoxList(R.id.four_check, R.id.four_text, new ArrayList<>(Arrays.asList(R.id.three_check,R.id.five_check)));
            ActivityResultLauncher<Intent> five = makeCheckBoxList(R.id.five_check, R.id.five_text, new ArrayList<>(Arrays.asList(R.id.four_check,R.id.three_check)));
            if (mode.equals(Constants.FARM_CREATE)) {
                makeCheckboxActive(R.id.three_check, R.id.three_text, 3, three);
                makeCheckboxActive(R.id.four_check, R.id.four_text, 4, four);
                makeCheckboxActive(R.id.five_check, R.id.five_text, 5, five);
                submit.setOnClickListener((View v) -> {
                    if (birthCount.getText().toString().isEmpty() ||
                            showerCount.getText().toString().isEmpty() ||
                            showerPitCount.getText().toString().isEmpty() ||
                            showerUnitCount.getText().toString().isEmpty() ||
                            scoreMethod == null
                    ) {
                        Toast.makeText(this, R.string.check_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Farm farm = new Farm();
                    farm.favorite = false;
                    farm.name = farmTitle.getText().toString();
                    farm.bedType = bedType.getText().toString();
                    farm.birthCount = Integer.parseInt(birthCount.getText().toString());
                    farm.showerCount = Integer.parseInt(showerCount.getText().toString());
                    farm.showerPitCount = Integer.parseInt(showerPitCount.getText().toString());
                    farm.showerUnitCount = Integer.parseInt(showerUnitCount.getText().toString());
                    if (farm.name.isEmpty() || farm.bedType.isEmpty()) {
                        Toast.makeText(this, R.string.check_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }


                    MyDao dao = DataBase.getInstance(this).dao();
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        farm.scoreMethodId = dao.insertGetId(scoreMethod);
                        dao.insert(farm);

                        hideKeyboard();
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
                    ScoreMethod tempScoreMethod = dao.getScoreMethod(farm.scoreMethodId);
                    runOnUiThread(() -> {
                        farmTitle.setText(farm.name);
                        bedType.setText(farm.bedType);
                        birthCount.setText("" + farm.birthCount);
                        showerCount.setText("" + farm.showerCount);
                        showerPitCount.setText("" + farm.showerPitCount);
                        showerUnitCount.setText("" + farm.showerUnitCount);
                        scoreMethod = tempScoreMethod;
                        scoreMethod.id = null;
                        switch (scoreMethod.scoresCount) {
                            case 3: {
                                CheckBox checkBox = findViewById(R.id.three_check);
                                checkBox.setChecked(true);
                                break;
                            }
                            case 4: {
                                CheckBox checkBox = findViewById(R.id.four_check);
                                checkBox.setChecked(true);
                                break;
                            }
                            case 5: {
                                CheckBox checkBox = findViewById(R.id.five_check);
                                checkBox.setChecked(true);
                                break;
                            }
                            default:
                                throw new IllegalStateException("Unexpected value: " + scoreMethod.scoresCount);
                        }
                        makeCheckboxActive(R.id.three_check, R.id.three_text, 3, three);
                        makeCheckboxActive(R.id.four_check, R.id.four_text, 4, four);
                        makeCheckboxActive(R.id.five_check, R.id.five_text, 5, five);

                    });
                });

                submit.setOnClickListener((View v) -> AppExecutors.getInstance().diskIO().execute(() -> {
                    Farm farm = dao.getFarm(id);
                    runOnUiThread(() -> {
                        farm.name = farmTitle.getText().toString();
                        farm.bedType = bedType.getText().toString();
                        farm.birthCount = Integer.parseInt(birthCount.getText().toString());
                        farm.showerCount = Integer.parseInt(showerCount.getText().toString());
                        farm.showerPitCount = Integer.parseInt(showerPitCount.getText().toString());
                        farm.showerUnitCount = Integer.parseInt(showerUnitCount.getText().toString());
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            farm.scoreMethodId = dao.insertGetId(scoreMethod);
                            dao.update(farm);
                            hideKeyboard();
                            runOnUiThread(this::finish);
                        });
                    });

                }));
            }

        }

    }

    public void hideKeyboard() {
        Constants.hideKeyboard(this, findViewById(R.id.root).getWindowToken());
    }

    private ActivityResultLauncher<Intent> makeCheckBoxList(Integer one, Integer oneText, ArrayList<Integer> others) {
        CheckBox checkBox_0 = findViewById(one);
        TextView textView_0 = findViewById(oneText);

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        this.scoreMethod = (ScoreMethod) data.getSerializableExtra(Constants.SCORE_METHOD_INTENT);
                        for (Integer integer: others){
                            ((CheckBox) findViewById(integer)).setChecked(false);
                        }
                    } else {
                        checkBox_0.setChecked(false);
                    }
                });

    }

    public void makeCheckboxActive(Integer one, Integer oneText, int count, ActivityResultLauncher<Intent> someActivityResultLauncher) {
        CheckBox checkBox_0 = findViewById(one);
        TextView textView_0 = findViewById(oneText);

        checkBox_0.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(AddFarmActivity.this, CreateScoreMethod.class);
                    intent.putExtra(Constants.SCORE_METHOD_INTENT_MODE, "CREATE");
                    intent.putExtra(Constants.SCORE_METHOD_INTENT_COUNT, count);
                    someActivityResultLauncher.launch(intent);
                });
            } else {
                scoreMethod = null;
            }
        });
        textView_0.setOnClickListener(view -> checkBox_0.setChecked(!checkBox_0.isChecked()));
    }

}