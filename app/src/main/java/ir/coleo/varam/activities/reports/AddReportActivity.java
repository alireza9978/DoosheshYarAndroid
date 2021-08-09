package ir.coleo.varam.activities.reports;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.badoualy.stepperindicator.StepperIndicator;

import java.util.ArrayList;
import java.util.List;
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
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.CheckBoxManager;
import ir.coleo.varam.models.DateContainer;

import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_CREATE;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_CREATE_END;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_RESULT;
import static ir.coleo.varam.constants.Constants.DRUG_SELECTION;

/**
 * صفحه مدیریت صفحان ثبت گزارش
 */
public class AddReportActivity extends AppCompatActivity {

    private State state;
    private Cow cow;
    private Farm farm;
    private ScoreMethod scoreMethod;
    private TabAdapterReport adapter;
    private StepperIndicator stepperIndicator;
    private DateContainer one;
    private DateContainer two;
    private int farmId;
    private String mode;
    private Integer reportId;
    private ViewPager2 viewPager;

    private ArrayList<Report> fastReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        Bundle bundle = Objects.requireNonNull(getIntent().getExtras());
        mode = bundle.getString(Constants.REPORT_MODE);
        assert mode != null;


        fastReports = new ArrayList<>();
        boolean persian = Constants.getDefaultLanguage(this).equals("fa");
        stepperIndicator = findViewById(R.id.state_indicator);
        if (persian) {
            stepperIndicator.setRotation(180);
            stepperIndicator.setRotationX(180);
            stepperIndicator.setShowDoneIcon(false);
        }
        viewPager = findViewById(R.id.pager_id);

        if (mode.equals(Constants.EDIT_REPORT)) {
            reportId = bundle.getInt(Constants.REPORT_ID);
            MyDao dao = DataBase.getInstance(this).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                Report report = dao.getReport(reportId);
                cow = dao.getCow(report.cowId);
                farm = dao.getFarm(cow.getFarm());
                scoreMethod = dao.getScoreMethod(farm.scoreMethodId);
                DateContainer container;
                DateContainer container_two;
                int[] temp = report.visit.convert(this);
                if (persian) {
                    DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                } else {
                    DateContainer.MyDate date = new DateContainer.MyDate(false, temp[2], temp[1], temp[0]);
                    container = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                }
                one = container;
                if (report.nextVisit != null) {
                    temp = report.nextVisit.convert(this);
                    if (persian) {
                        DateContainer.MyDate date = new DateContainer.MyDate(true, temp[2], temp[1], temp[0]);
                        container_two = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                    } else {
                        DateContainer.MyDate date = new DateContainer.MyDate(false, temp[2], temp[1], temp[0]);
                        container_two = new DateContainer(Constants.DateSelectionMode.SINGLE, date);
                    }
                    two = container_two;
                }

                runOnUiThread(() -> {
                    state = State.info;
                    String nextDate = null;
                    if (two != null) {
                        nextDate = two.toString(this);
                    }
                    CheckBoxManager.getCheckBoxManager(scoreMethod).setBooleansFromReport(report, scoreMethod);
                    ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
                    if (report.pomadeId != null) {
                        list.add(new Pair<>(0, report.pomadeId));
                    }
                    if (report.antibioticId != null) {
                        list.add(new Pair<>(1, report.antibioticId));
                    }
                    if (report.serumId != null) {
                        list.add(new Pair<>(2, report.serumId));
                    }
                    if (report.cureId != null) {
                        list.add(new Pair<>(3, report.cureId));
                    }
                    if (report.antiInflammatoryId != null) {
                        list.add(new Pair<>(4, report.antiInflammatoryId));
                    }
                    adapter = new TabAdapterReport(this, cow.getNumber(),
                            one.toString(this), nextDate, report.areaNumber - 1,
                            report.description, scoreMethod, list);

                    viewPager.setOffscreenPageLimit(2);
                    viewPager.setUserInputEnabled(false);
                    viewPager.setAdapter(adapter);

                    stepperIndicator = findViewById(R.id.state_indicator);
                    ImageView exit = findViewById(R.id.close_image);
                    ImageView moreInfo = findViewById(R.id.info_image);

                    exit.setOnClickListener(view -> finish());
                    moreInfo.setOnClickListener(view -> {
                        Intent intent = new Intent(this, MoreInfoActivity.class);
                        intent.putExtra(Constants.MORE_INFO_STATE, state);
                        startActivity(intent);
                    });
                });
            });

        } else if (mode.equals(Constants.REPORT_CREATE)) {
            int id = bundle.getInt(Constants.COW_ID, -1);
            if (id == -1) {
                farmId = bundle.getInt(Constants.FARM_ID);
            }

            MyDao dao = DataBase.getInstance(this).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                if (id != -1) {
                    cow = dao.getCow(id);
                    farm = dao.getFarm(cow.getFarm());
                } else {
                    cow = null;
                    farm = dao.getFarm(farmId);
                }
                scoreMethod = dao.getScoreMethod(farm.scoreMethodId);
                runOnUiThread(() -> {

                    state = State.info;
                    adapter = new TabAdapterReport(this, scoreMethod);

                    viewPager.setOffscreenPageLimit(2);
                    viewPager.setUserInputEnabled(false);
                    viewPager.setAdapter(adapter);

                    stepperIndicator = findViewById(R.id.state_indicator);
                    ImageView exit = findViewById(R.id.close_image);
                    ImageView moreInfo = findViewById(R.id.info_image);

                    exit.setOnClickListener(view -> finish());
                    moreInfo.setOnClickListener(view -> {
                        Intent intent = new Intent(this, MoreInfoActivity.class);
                        intent.putExtra(Constants.MORE_INFO_STATE, state);
                        startActivity(intent);
                    });
                    one = DateContainer.getToday(this, persian);
                    ((CowInfoFragment) adapter.getFragment(0)).setDate(one.toString(this));

                    if (cow != null)
                        ((CowInfoFragment) adapter.getFragment(0)).setCowNumber(cow.getNumber());

                });
            });
        }
    }

    public void hideKeyboard() {
        Constants.hideKeyboard(this, findViewById(R.id.root).getWindowToken());
    }

    private void addCowAndReport() {
        MyDao dao = DataBase.getInstance(this).dao();
        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMethod);
        Report report = new Report();
        report.visit = one.exportStart();
        if (two != null) {
            report.nextVisit = two.exportStart();
        }
        report.scoreMethodId = farm.scoreMethodId;
        report.areaNumber = ((CowInjuryFragment) adapter.getFragment(1)).getSelected() + 1;
        manager.setBooleansOnReport(report);
        report.description = ((MoreInfoFragment) adapter.getFragment(3)).getMoreInfo();
        fastReports.add(report);

        AppExecutors.getInstance().diskIO().execute(() -> {
            if (manager.isContinueCure()) {
                if (cow != null) {
                    List<Report> reports = dao.getReportOfCowWithDrug(cow.getId());
                    if (reports != null && reports.size() > 0) {
                        Report temp = reports.get(0);
                        for (Report innerReport : fastReports) {
                            innerReport.pomadeId = temp.pomadeId;
                            innerReport.serumId = temp.serumId;
                            innerReport.antibioticId = temp.antibioticId;
                            innerReport.antiInflammatoryId = temp.antiInflammatoryId;
                            innerReport.cureId = temp.cureId;
                        }
                    }
                }else{
                    runOnUiThread(() -> Toast.makeText(this, getString(R.string.new_cow_cure_error), Toast.LENGTH_SHORT).show());
                    ((CowInjuryFragment) adapter.getFragment(1)).reset();
                    return;
                }

            } else {
                for (Report tempReport : fastReports) {
                    ((DrugFragment) adapter.getFragment(2)).setDrugOnReport(tempReport);
                }
            }

            if (mode.equals(Constants.REPORT_CREATE)) {
                if (cow == null) {
                    Integer cowNumber = ((CowInfoFragment) adapter.getFragment(0)).getNumber();
                    cow = dao.getCow(cowNumber, farmId);
                    if (cow == null) {
                        cow = new Cow(cowNumber, false, farmId);
                        cow.setId((int) dao.insertGetId(cow));
                    }
                }
                report.cowId = cow.getId();
                for (Report tempReport : fastReports) {
                    dao.insert(tempReport);
                }
                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.report_added), Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                report.cowId = cow.getId();
                report.id = reportId;
                dao.update(report);
                runOnUiThread(() -> {
                    Toast.makeText(this, getString(R.string.report_updated), Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });

    }

    public void addCowAndReportFast() {
        MyDao dao = DataBase.getInstance(this).dao();

        Report report = new Report();
        report.visit = one.exportStart();
        if (two != null) {
            report.nextVisit = two.exportStart();
        }
        report.scoreMethodId = farm.scoreMethodId;
        report.areaNumber = ((CowInjuryFragment) adapter.getFragment(1)).getSelected() + 1;
        CheckBoxManager.getCheckBoxManager(scoreMethod).setBooleansOnReport(report);

        if (mode.equals(Constants.REPORT_CREATE)) {
            AppExecutors.getInstance().diskIO().execute(() -> {
                if (cow == null) {
                    Integer cowNumber = ((CowInfoFragment) adapter.getFragment(0)).getNumber();
                    cow = dao.getCow(cowNumber, farmId);
                    if (cow == null) {
                        cow = new Cow(cowNumber, false, farmId);
                        cow.setId((int) dao.insertGetId(cow));
                    }
                }
                report.cowId = cow.getId();
                runOnUiThread(() -> {
                    fastReports.add(report);
                    Toast.makeText(this, getString(R.string.report_added), Toast.LENGTH_SHORT).show();
                });
            });
        }
    }

    public void next() {
        switch (state) {
            case info:
                state = State.injury;
                break;
            case injury:
                if (goingDrugPage()) {
                    state = State.drugs;
                } else {
                    addCowAndReport();
                    return;
                }
                break;
            case drugs:
                addCowAndReport();
                return;
        }
        viewPager.setCurrentItem(State.getNumber(state));
        stepperIndicator.setCurrentStep(State.getNumber(state));
    }

    private boolean goingDrugPage() {
        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMethod);
        return !manager.isTarkhis() && !manager.isRest() && !manager.isContinueCure();
    }

    public void back() {
        switch (state) {
            case info:
                break;
            case injury:
                state = State.info;
                break;
            case drugs:
                state = State.injury;
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
                    ((MoreInfoFragment) adapter.getFragment(3)).setDate(container.toString(this));
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
                    ((DrugFragment) adapter.getFragment(2)).setDrug(drugType, drugId);
                    break;
                }
            }
            default: {
                break;
            }
        }
    }

}