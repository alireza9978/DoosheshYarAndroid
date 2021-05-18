package ir.coleo.varam.activities.tabs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.utils.AppExecutors;

/**
 * صفحه ایجاد یک گاوداری جدید
 */
public class AddFarmActivity extends AppCompatActivity {

    private Boolean dryMethod = null;
    private Boolean scoreMethod = null;

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
            makeCheckBoxList(R.id.one_check, R.id.one_text, R.id.two_check, R.id.two_text, true);
            makeCheckBoxList(R.id.three_check, R.id.three_text, R.id.four_check, R.id.four_text, false);
            if (mode.equals(Constants.FARM_CREATE)) {
                submit.setOnClickListener((View v) -> {
                    if (birthCount.getText().toString().isEmpty() ||
                            showerCount.getText().toString().isEmpty() ||
                            showerPitCount.getText().toString().isEmpty() ||
                            showerUnitCount.getText().toString().isEmpty() ||
                            dryMethod == null || scoreMethod == null
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
                    farm.dryMethod = dryMethod;
                    farm.scoreMethod = scoreMethod;
                    if (farm.name.isEmpty() || farm.bedType.isEmpty()) {
                        Toast.makeText(this, R.string.check_fields, Toast.LENGTH_SHORT).show();
                        return;
                    }


                    MyDao dao = DataBase.getInstance(this).dao();
                    AppExecutors.getInstance().diskIO().execute(() -> {
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
                    runOnUiThread(() -> {
                        farmTitle.setText(farm.name);
                        bedType.setText(farm.bedType);
                        birthCount.setText("" + farm.birthCount);
                        showerCount.setText("" + farm.showerCount);
                        showerPitCount.setText("" + farm.showerPitCount);
                        showerUnitCount.setText("" + farm.showerUnitCount);
                        this.dryMethod = farm.dryMethod;
                        this.scoreMethod = farm.scoreMethod;
                        if (this.dryMethod != null)
                            if (this.dryMethod) {
                                CheckBox checkBox = findViewById(R.id.one_check);
                                checkBox.setChecked(true);
                            } else {
                                CheckBox checkBox = findViewById(R.id.two_check);
                                checkBox.setChecked(true);
                            }
                        if (this.scoreMethod) {
                            CheckBox checkBox = findViewById(R.id.three_check);
                            checkBox.setChecked(true);
                        } else {
                            CheckBox checkBox = findViewById(R.id.four_check);
                            checkBox.setChecked(true);
                        }
                    });
                });

                submit.setOnClickListener((View v) -> {
                    AppExecutors.getInstance().diskIO().execute(() -> {
                        Farm farm = dao.getFarm(id);
                        runOnUiThread(() -> {
                            farm.name = farmTitle.getText().toString();
                            farm.bedType = bedType.getText().toString();
                            farm.birthCount = Integer.parseInt(birthCount.getText().toString());
                            farm.showerCount = Integer.parseInt(showerCount.getText().toString());
                            farm.showerPitCount = Integer.parseInt(showerPitCount.getText().toString());
                            farm.showerUnitCount = Integer.parseInt(showerUnitCount.getText().toString());
                            farm.dryMethod = dryMethod;
                            farm.scoreMethod = scoreMethod;
                            AppExecutors.getInstance().diskIO().execute(() -> {
                                dao.update(farm);
                                hideKeyboard();
                                runOnUiThread(this::finish);
                            });
                        });

                    });
                });
            }

        }

    }

    public void hideKeyboard() {
        Constants.hideKeyboard(this, findViewById(R.id.root).getWindowToken());
    }

    private void makeCheckBoxList(Integer one, Integer oneText, Integer two, Integer twoText, boolean up) {
        CheckBox checkBox_0 = findViewById(one);
        TextView textView_0 = findViewById(oneText);
        CheckBox checkBox_1 = findViewById(two);
        TextView textView_1 = findViewById(twoText);

        checkBox_0.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                checkBox_1.setChecked(false);
                if (up) {
                    dryMethod = Boolean.TRUE;
                } else {
                    scoreMethod = Boolean.TRUE;
                }
            } else {
                if (up) {
                    dryMethod = null;
                } else {
                    scoreMethod = null;
                }
            }
        });
        checkBox_1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                checkBox_0.setChecked(false);
                if (up) {
                    dryMethod = Boolean.FALSE;
                } else {
                    scoreMethod = Boolean.FALSE;
                }
            } else {
                if (up) {
                    dryMethod = null;
                } else {
                    scoreMethod = null;
                }
            }
        });
        textView_0.setOnClickListener(view -> {
            checkBox_0.setChecked(!checkBox_0.isChecked());
        });
        textView_1.setOnClickListener(view -> {
            checkBox_1.setChecked(!checkBox_1.isChecked());
        });
    }

}