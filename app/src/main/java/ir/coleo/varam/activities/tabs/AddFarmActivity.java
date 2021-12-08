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
            Button scoresLevelText = findViewById(R.id.scores_level_text);

            farmTitle.requestFocus();
            Button submit = findViewById(R.id.submit);

            ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            assert data != null;
                            this.scoreMethod = (ScoreMethod) data.getSerializableExtra(Constants.SCORE_METHOD_INTENT);
                            switch (scoreMethod.scoresCount) {
                                case 3: {
                                    scoresLevelText.setText(R.string.define_levels_three);
                                    break;
                                }
                                case 4: {
                                    scoresLevelText.setText(R.string.define_levels_four);
                                    break;
                                }
                                case 5: {
                                    scoresLevelText.setText(R.string.define_levels_five);
                                    break;
                                }
                                case 6: {
                                    scoresLevelText.setText(R.string.define_levels_six);
                                    break;
                                }
                                case 7: {
                                    scoresLevelText.setText(R.string.define_levels_seven);
                                    break;
                                }
                                case 8: {
                                    scoresLevelText.setText(R.string.define_levels_eight);
                                    break;
                                }
                                default:
                                    throw new IllegalStateException("Unexpected value: " + scoreMethod.scoresCount);
                            }
                        } else {
                            scoresLevelText.setText(R.string.define_levels);
                        }
                    });

            scoresLevelText.setOnClickListener(v -> {
                Intent intent = new Intent(AddFarmActivity.this, CreateScoreMethod.class);
                intent.putExtra(Constants.SCORE_METHOD_INTENT_MODE, "CREATE");
                someActivityResultLauncher.launch(intent);
            });
            if (mode.equals(Constants.FARM_CREATE)) {
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
                                scoresLevelText.setText(R.string.define_levels_three);
                                break;
                            }
                            case 4: {
                                scoresLevelText.setText(R.string.define_levels_four);
                                break;
                            }
                            case 5: {
                                scoresLevelText.setText(R.string.define_levels_five);
                                break;
                            }
                            default:
                                throw new IllegalStateException("Unexpected value: " + scoreMethod.scoresCount);
                        }
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
    

}