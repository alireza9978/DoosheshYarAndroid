package ir.coleo.varam.models;

import ir.coleo.varam.R;
import ir.coleo.varam.database.models.Report;

import java.util.ArrayList;

public class CheckBoxManager {

    private static CheckBoxManager checkBoxManager;
    private ArrayList<CheckBoxItem> reasons;
    private ArrayList<CheckBoxItem> moreInfo;

    private CheckBoxManager() {
        moreInfo = new ArrayList<>();
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_1));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_2));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_3));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_4));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_5));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_6));
        moreInfo.add(new CheckBoxItem(R.string.more_info_reason_7));
        moreInfo.get(0).add(moreInfo.get(1));
        moreInfo.get(0).add(moreInfo.get(2));
        moreInfo.get(0).add(moreInfo.get(3));
        moreInfo.get(1).add(moreInfo.get(0));
        moreInfo.get(1).add(moreInfo.get(3));
        moreInfo.get(1).add(moreInfo.get(4));
        moreInfo.get(2).add(moreInfo.get(0));
        moreInfo.get(2).add(moreInfo.get(4));
        moreInfo.get(2).add(moreInfo.get(5));
        moreInfo.get(3).add(moreInfo.get(0));
        moreInfo.get(3).add(moreInfo.get(1));
        moreInfo.get(3).add(moreInfo.get(4));
        moreInfo.get(3).add(moreInfo.get(5));

        reasons = new ArrayList<>();
        reasons.add(new CheckBoxItem(R.string.reason_1));
        reasons.add(new CheckBoxItem(R.string.reason_2));
        reasons.add(new CheckBoxItem(R.string.reason_3));
        reasons.add(new CheckBoxItem(R.string.reason_4));
        reasons.add(new CheckBoxItem(R.string.reason_5));
        reasons.add(new CheckBoxItem(R.string.reason_6));
        reasons.add(new CheckBoxItem(R.string.reason_7));
        reasons.add(new CheckBoxItem(R.string.reason_8));
        reasons.add(new CheckBoxItem(R.string.reason_9));
        reasons.add(new CheckBoxItem(R.string.reason_10));
        reasons.get(0).add(reasons.get(1));
        reasons.get(0).add(reasons.get(2));
        reasons.get(1).add(reasons.get(0));
        reasons.get(1).add(reasons.get(2));
        reasons.get(2).add(reasons.get(0));
        reasons.get(2).add(reasons.get(1));
        reasons.get(3).add(reasons.get(4));
        reasons.get(4).add(reasons.get(3));

    }

    public static CheckBoxManager getCheckBoxManager() {
        if (checkBoxManager == null) {
            checkBoxManager = new CheckBoxManager();
        }
        return checkBoxManager;
    }

    private void reset() {
        for (CheckBoxItem item : moreInfo) {
            item.setCheck(false);
            item.setActive(true);
        }
        for (CheckBoxItem item : reasons) {
            item.setCheck(false);
            item.setActive(true);
        }
    }

    public boolean moreInfoSelected() {
        return !(moreInfo.get(0).isCheck() || moreInfo.get(1).isCheck() || moreInfo.get(6).isCheck());
    }

    public boolean reasonSelected() {
        for (CheckBoxItem item : reasons) {
            if (item.isCheck() && item.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void setBooleansFromReport(Report report) {
        reset();
        reasons.get(0).setCheck(report.referenceCauseHundredDays);
        reasons.get(1).setCheck(report.referenceCauseDryness);
        reasons.get(2).setCheck(report.referenceCauseLagged);
        reasons.get(3).setCheck(report.referenceCauseNewLimp);
        reasons.get(4).setCheck(report.referenceCauseLimpVisit);
        reasons.get(5).setCheck(report.referenceCauseHighScore);
        reasons.get(6).setCheck(report.referenceCauseReferential);
        reasons.get(7).setCheck(report.referenceCauseLongHoof);
        reasons.get(8).setCheck(report.referenceCauseHeifer);
        reasons.get(9).setCheck(report.referenceCauseGroupHoofTrim);

        moreInfo.get(0).setCheck(report.otherInfoWound);
        moreInfo.get(1).setCheck(report.otherInfoEcchymosis);
        moreInfo.get(2).setCheck(report.otherInfoRecovered);
        moreInfo.get(3).setCheck(report.otherInfoNoInjury);
        moreInfo.get(4).setCheck(report.otherInfoGel);
        moreInfo.get(5).setCheck(report.otherInfoBoarding);
        moreInfo.get(6).setCheck(report.otherInfoHoofTrim);
    }

    public void setBooleansOnReport(Report report) {
        report.referenceCauseHundredDays = reasons.get(0).isCheck();
        report.referenceCauseDryness = reasons.get(1).isCheck();
        report.referenceCauseLagged = reasons.get(2).isCheck();
        report.referenceCauseNewLimp = reasons.get(3).isCheck();
        report.referenceCauseLimpVisit = reasons.get(4).isCheck();
        report.referenceCauseHighScore = reasons.get(5).isCheck();
        report.referenceCauseReferential = reasons.get(6).isCheck();
        report.referenceCauseLongHoof = reasons.get(7).isCheck();
        report.referenceCauseHeifer = reasons.get(8).isCheck();
        report.referenceCauseGroupHoofTrim = reasons.get(9).isCheck();

        report.otherInfoWound = moreInfo.get(0).isCheck();
        report.otherInfoEcchymosis = moreInfo.get(1).isCheck();
        report.otherInfoRecovered = moreInfo.get(2).isCheck();
        report.otherInfoNoInjury = moreInfo.get(3).isCheck();
        report.otherInfoGel = moreInfo.get(4).isCheck();
        report.otherInfoBoarding = moreInfo.get(5).isCheck();
        report.otherInfoHoofTrim = moreInfo.get(6).isCheck();
        reset();
    }

    public ArrayList<CheckBoxItem> getReasons() {
        return reasons;
    }

    public ArrayList<CheckBoxItem> getMoreInfo() {
        return moreInfo;
    }
}
