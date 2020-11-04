package ir.coleo.varam.activities.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.fragments.CartieStateFragment;
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
import ir.coleo.varam.models.DateContainer;
import ir.coleo.varam.ui_element.MyViewPager;

import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_CREATE;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_CREATE_END;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_RESULT;
import static ir.coleo.varam.constants.Constants.DRUG_SELECTION;

public class AddReportActivity extends AppCompatActivity {

    private State state;
    private Cow cow;
    private TabAdapterReport adapter;
    private TabLayout tabLayout;
    private StepperIndicator stepperIndicator;
    private int fingerNumber;
    private DateContainer one;
    private DateContainer two;
    private int farmId;
    private String mode;
    private Integer reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        Bundle bundle = Objects.requireNonNull(getIntent().getExtras());
        mode = bundle.getString(Constants.REPORT_MODE);

        assert mode != null;
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
                Farm farm;
                if (id != -1) {
                    cow = dao.getCow(id);
                    farm = dao.getFarm(cow.getFarm());
                    if (cow != null)
                        ((CowInfoFragment) adapter.getItem(0)).setCowNumber(cow.getNumber());
                } else {
                    cow = null;
                    farm = dao.getFarm(farmId);
                }

                runOnUiThread(() -> {
                    tabLayout = new TabLayout(this);
                    state = State.info;
                    adapter = new TabAdapterReport(this, getSupportFragmentManager());
                    adapter.addFragment(new CowInfoFragment());
                    adapter.addFragment(new CowInjuryFragment(farm.scoreMethod));
                    adapter.addFragment(new CartieStateFragment(farm.scoreMethod));
                    adapter.addFragment(new DrugFragment());
                    adapter.addFragment(new MoreInfoFragment());

                    MyViewPager viewPager = findViewById(R.id.pager_id);
                    viewPager.setAdapter(adapter);
                    viewPager.setEnableSwipe(false);
                    tabLayout.setupWithViewPager(viewPager);

                    stepperIndicator = findViewById(R.id.state_indicator);
                    ImageView exit = findViewById(R.id.close_image);
                    exit.setOnClickListener(view -> finish());
                });

            });


        }


    }

    private void addCowAndReport() {
        MyDao dao = DataBase.getInstance(this).dao();
        if (mode.equals(Constants.EDIT_REPORT)) {

        } else {

        }
    }

    public void next() {
        switch (state) {
            case info:
                state = State.injury;
                break;
            case cartieState:
                state = State.drugs;
                break;
            case injury:
                state = State.cartieState;
                break;
            case drugs:
                state = State.moreInfo;
                break;
            case moreInfo:
                addCowAndReport();
                return;
        }
        tabLayout.selectTab(tabLayout.getTabAt(State.getNumber(state)));
        stepperIndicator.setCurrentStep(State.getNumber(state));
    }

    public void back() {
        switch (state) {
            case info:
                break;
            case cartieState:
                state = State.info;
                break;
            case injury:
                state = State.cartieState;
                break;
            case drugs:
                state = State.injury;
                break;
            case moreInfo:
                state = State.drugs;
                break;
        }
        tabLayout.selectTab(tabLayout.getTabAt(State.getNumber(state)));
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
                    ((CowInfoFragment) adapter.getItem(0)).setDate(container.toString(this));
                }
                break;
            }
            case DATE_SELECTION_REPORT_CREATE_END: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(DATE_SELECTION_RESULT);
                    assert container != null;
                    two = container;
                    ((MoreInfoFragment) adapter.getItem(3)).setDate(container.toString(this));
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
                    ((DrugFragment) adapter.getItem(3)).setDrug(drugType, drugId);
                    break;
                }
            }
            default: {
                break;
            }
        }
    }

}