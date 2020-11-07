package ir.coleo.varam.activities.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.badoualy.stepperindicator.StepperIndicator;

import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.fragments.CowInfoFragment;
import ir.coleo.varam.activities.reports.fragments.CowInjuryFragment;
import ir.coleo.varam.activities.reports.fragments.DrugFragment;
import ir.coleo.varam.activities.reports.fragments.MoreInfoFragment;
import ir.coleo.varam.adapters.TabAdapterReport;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Cow;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.CheckBoxManager;
import ir.coleo.varam.models.DateContainer;

import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_CREATE;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_CREATE_END;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_RESULT;
import static ir.coleo.varam.constants.Constants.DRUG_SELECTION;

public class AddReportActivity extends AppCompatActivity {

    private State state;
    private Cow cow;
    private TabAdapterReport adapter;
    private StepperIndicator stepperIndicator;
    private DateContainer one;
    private DateContainer two;
    private int farmId;
    private String mode;
    private Integer reportId;
    private ViewPager2 viewPager;
    private Farm farm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        Bundle bundle = Objects.requireNonNull(getIntent().getExtras());
        mode = bundle.getString(Constants.REPORT_MODE);
        assert mode != null;

        boolean persian = Constants.getDefaultLanguage(this).equals("fa");
        stepperIndicator = findViewById(R.id.state_indicator);
        if (persian) {
            stepperIndicator.setRotation(180);
            stepperIndicator.setRotationX(180);
            stepperIndicator.setShowDoneIcon(false);
        }
        viewPager = findViewById(R.id.pager_id);

        if (mode.equals(Constants.EDIT_REPORT)) {
            Log.i("ADD_REPORT", "onCreate: edit mode");
            reportId = bundle.getInt(Constants.REPORT_ID);
            MyDao dao = DataBase.getInstance(this).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                Report report = dao.getReport(reportId);
            });
        } else if (mode.equals(Constants.REPORT_CREATE)) {
            Log.i("ADD_REPORT", "onCreate: add mode");

            int id = bundle.getInt(Constants.COW_ID, -1);
            if (id == -1) {
                farmId = bundle.getInt(Constants.FARM_ID);
            }

            MyDao dao = DataBase.getInstance(this).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                if (id != -1) {
                    cow = dao.getCow(id);
                    farm = dao.getFarm(cow.getFarm());
                    if (cow != null)
                        ((CowInfoFragment) adapter.getFragment(0)).setCowNumber(cow.getNumber());
                } else {
                    cow = null;
                    farm = dao.getFarm(farmId);
                }

                runOnUiThread(() -> {

                    state = State.info;
                    adapter = new TabAdapterReport(this, farm.scoreMethod);

                    viewPager.setOffscreenPageLimit(2);
                    viewPager.setUserInputEnabled(false);
                    viewPager.setAdapter(adapter);

                    stepperIndicator = findViewById(R.id.state_indicator);
                    ImageView exit = findViewById(R.id.close_image);
                    exit.setOnClickListener(view -> finish());
                    one = DateContainer.getToday(this, persian);
                    ((CowInfoFragment) adapter.getFragment(0)).setDate(one.toString(this));
                });

            });


        }


    }

    private void addCowAndReport() {
        MyDao dao = DataBase.getInstance(this).dao();
        if (mode.equals(Constants.REPORT_CREATE)) {
            Report report = new Report();
            report.visit = one.exportStart();
            if (two != null) {
                report.nextVisit = two.exportStart();
            }
            report.scoreType = farm.scoreMethod;
            report.areaNumber = ((CowInjuryFragment) adapter.getFragment(1)).getSelected();
            ((DrugFragment) adapter.getFragment(3)).setDrugOnReport(report);
            CheckBoxManager.getCheckBoxManager(farm.scoreMethod).setBooleansOnReport(report);
            report.description = ((MoreInfoFragment) adapter.getFragment(4)).getMoreInfo();
            AppExecutors.getInstance().diskIO().execute(() -> {
                if (cow == null) {
                    Integer cowNumber = ((CowInfoFragment) adapter.getFragment(0)).getNumber();
                    cow = new Cow(cowNumber, false, farmId);
                    cow.setId((int) dao.insertGetId(cow));
                }
                report.cowId = cow.getId();
                dao.insert(report);
                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.report_added), Toast.LENGTH_SHORT).show();
                    finish();
                });

            });
        } else {
        }
    }

    public void next() {
        switch (state) {
            case info:
                state = State.injury;
                break;
            case injury:
                state = State.cartieState;
                break;
            case cartieState:
                state = State.drugs;
                break;
            case drugs:
                state = State.moreInfo;
                break;
            case moreInfo:
                addCowAndReport();
                return;
        }
        viewPager.setCurrentItem(State.getNumber(state));
        stepperIndicator.setCurrentStep(State.getNumber(state));
    }

    public void back() {
        switch (state) {
            case info:
                break;
            case injury:
                state = State.info;
                break;
            case cartieState:
                state = State.injury;
                break;
            case drugs:
                state = State.cartieState;
                break;
            case moreInfo:
                state = State.drugs;
                break;
        }
        viewPager.setCurrentItem(State.getNumber(state));
        stepperIndicator.setCurrentStep(State.getNumber(state));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case DATE_SELECTION_REPORT_CREATE: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(DATE_SELECTION_RESULT);
                    assert container != null;
                    one = container;
                    ((CowInfoFragment) adapter.getFragment(0)).setDate(container.toString(this));
                }
                break;
            }
            case DATE_SELECTION_REPORT_CREATE_END: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(DATE_SELECTION_RESULT);
                    assert container != null;
                    two = container;
                    ((MoreInfoFragment) adapter.getFragment(4)).setDate(container.toString(this));
                }
                break;
            }
            case DRUG_SELECTION: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    Bundle bundle = data.getExtras();
                    assert bundle != null;
                    int drugId = bundle.getInt(Constants.DRUG_ID, -1);
                    int drugType = bundle.getInt(Constants.DRUG_TYPE, -1);
                    ((DrugFragment) adapter.getFragment(3)).setDrug(drugType, drugId);
                    break;
                }
            }
            default: {
                break;
            }
        }
    }

}