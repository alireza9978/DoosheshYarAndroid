package ir.coleo.varam.activities.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.badoualy.stepperindicator.StepperIndicator;
import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.fragments.CowInfoFragment;
import ir.coleo.varam.activities.reports.fragments.CowInjuryFragment;
import ir.coleo.varam.activities.reports.fragments.CowReasonFragment;
import ir.coleo.varam.activities.reports.fragments.MoreInfoFragment;
import ir.coleo.varam.adapters.TabAdapterReport;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.Cow;
import ir.coleo.varam.database.models.Report;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.CheckBoxManager;
import ir.coleo.varam.models.DateContainer;
import ir.coleo.varam.ui_element.MyViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_CREATE;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_CREATE_END;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_RESULT;

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
                DateContainer container;
                DateContainer container_two;
                int[] temp = report.visit.convert(this);
                if (Constants.getDefualtlanguage(this).equals("fa")) {
                    DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                } else {
                    DateContainer.MyDate date = new DateContainer.MyDate(false, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                }
                one = container;
                if (report.nextVisit != null) {
                    temp = report.nextVisit.convert(this);
                    if (Constants.getDefualtlanguage(this).equals("fa")) {
                        DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                        container_two = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                    } else {
                        DateContainer.MyDate date = new DateContainer.MyDate(false, temp[2], temp[1], temp[0]);
                        container_two = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                    }
                    two = container_two;
                }
                cow = dao.getCow(report.cowId);
                runOnUiThread(() -> {
                    CheckBoxManager.getCheckBoxManager().setBooleansFromReport(report);
                    fingerNumber = report.fingerNumber;

                    tabLayout = new TabLayout(this);
                    state = State.info;
                    adapter = new TabAdapterReport(this, getSupportFragmentManager());
                    adapter.addFragment(new CowInfoFragment(cow.getNumber(), report.visit.toString(this)));
                    adapter.addFragment(new CowReasonFragment());
                    adapter.addFragment(new CowInjuryFragment(report.legAreaNumber, report.rightSide));
                    if (two != null)
                        adapter.addFragment(new MoreInfoFragment(two.toString(this)));
                    else
                        adapter.addFragment(new MoreInfoFragment());

                    MyViewPager viewPager = findViewById(R.id.pager_id);
                    viewPager.setAdapter(adapter);
                    viewPager.setEnableSwipe(false);
                    tabLayout.setupWithViewPager(viewPager);

                    stepperIndicator = findViewById(R.id.state_indicator);
                    ImageView moreInfo = findViewById(R.id.info_image);
                    ImageView exit = findViewById(R.id.close_image);

                    exit.setOnClickListener(view -> finish());
                    moreInfo.setOnClickListener(view -> {
                        Intent intent = new Intent(this, MoreInfoActivity.class);
                        intent.putExtra(Constants.MORE_INFO_STATE, state);
                        startActivity(intent);
                    });

                });


            });
        } else if (mode.equals(Constants.REPORT_CREATE)) {
            Log.i("ADD_REPORT", "onCreate: add mode");

            int id = bundle.getInt(Constants.COW_ID, -1);
            if (id == -1) {
                farmId = bundle.getInt(Constants.FARM_ID);
            }

            tabLayout = new TabLayout(this);
            state = State.info;
            adapter = new TabAdapterReport(this, getSupportFragmentManager());
            adapter.addFragment(new CowInfoFragment());
            adapter.addFragment(new CowReasonFragment());
            adapter.addFragment(new CowInjuryFragment());
            adapter.addFragment(new MoreInfoFragment());

            MyViewPager viewPager = findViewById(R.id.pager_id);
            viewPager.setAdapter(adapter);
            viewPager.setEnableSwipe(false);
            tabLayout.setupWithViewPager(viewPager);

            stepperIndicator = findViewById(R.id.state_indicator);
            ImageView moreInfo = findViewById(R.id.info_image);
            ImageView exit = findViewById(R.id.close_image);

            exit.setOnClickListener(view -> finish());
            moreInfo.setOnClickListener(view -> {
                Intent intent = new Intent(this, MoreInfoActivity.class);
                intent.putExtra(Constants.MORE_INFO_STATE, state);
                startActivity(intent);
            });


            MyDao dao = DataBase.getInstance(this).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                if (id != -1) {
                    cow = dao.getCow(id);
                    if (cow != null)
                        ((CowInfoFragment) adapter.getItem(0)).setCowNumber(cow.getNumber());
                } else {
                    cow = null;
                }
            });


        }


    }

    private void addCowAndReport() {
        MyDao dao = DataBase.getInstance(this).dao();
        if (mode.equals(Constants.EDIT_REPORT)) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                Log.i("ADD_REPORT", "onCreate: 3");

                Report report = new Report();
                report.visit = one.exportStart();
                if (two != null) {
                    report.nextVisit = two.exportStart();
                } else {
                    report.nextVisit = null;
                }
                report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                report.rightSide = ((CowInjuryFragment) adapter.getItem(2)).getRightSide();
                report.fingerNumber = this.fingerNumber;
                report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                report.cowId = cow.getId();
                CheckBoxManager.getCheckBoxManager().setBooleansOnReport(report);
                report.id = reportId;
                dao.update(report);
                runOnUiThread(this::finish);
            });


        } else {
            if (cow != null) {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Log.i("ADD_REPORT", "onCreate: 1");
                    Report report = new Report();
                    report.visit = one.exportStart();
                    if (two != null) {
                        report.nextVisit = two.exportStart();
                    } else {
                        report.nextVisit = null;
                    }
                    report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                    report.fingerNumber = this.fingerNumber;
                    report.rightSide = ((CowInjuryFragment) adapter.getItem(2)).getRightSide();
                    report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                    report.cowId = cow.getId();
                    CheckBoxManager.getCheckBoxManager().setBooleansOnReport(report);
                    dao.insert(report);
                    runOnUiThread(this::finish);

                });
            } else {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Log.i("ADD_REPORT", "onCreate: 2");
                    int cowNumber = ((CowInfoFragment) adapter.getItem(0)).getNumber();
                    Cow cow = dao.getCow(cowNumber, farmId);
                    if (cow == null) {
                        cow = new Cow(cowNumber, false, farmId);
                        dao.insert(cow);
                    }
                    cow = dao.getCow(cowNumber, farmId);
                    Report report = new Report();
                    report.visit = one.exportStart();
                    if (two != null) {
                        report.nextVisit = two.exportStart();
                    } else {
                        report.nextVisit = null;
                    }
                    report.legAreaNumber = ((CowInjuryFragment) adapter.getItem(2)).getSelected();
                    report.rightSide = ((CowInjuryFragment) adapter.getItem(2)).getRightSide();
                    report.fingerNumber = this.fingerNumber;
                    report.description = ((MoreInfoFragment) adapter.getItem(3)).getMoreInfo();
                    report.cowId = cow.getId();
                    CheckBoxManager.getCheckBoxManager().setBooleansOnReport(report);
                    dao.insert(report);
                    runOnUiThread(this::finish);
                });
            }
        }
    }

    public void next() {
        switch (state) {
            case info:
                state = State.reason;
                break;
            case reason:
                state = State.injury;
                break;
            case injury:
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
            case reason:
                state = State.info;
                break;
            case injury:
                state = State.reason;
                break;
            case moreInfo:
                state = State.injury;
                break;
        }
        tabLayout.selectTab(tabLayout.getTabAt(State.getNumber(state)));
        stepperIndicator.setCurrentStep(State.getNumber(state));
    }

    public void setFingerNumber(int fingerNumber) {
        this.fingerNumber = fingerNumber;
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
            default: {
                break;
            }
        }
    }

}