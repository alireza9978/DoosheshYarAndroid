package ir.coleo.varam.models;

import android.util.Log;

import java.util.ArrayList;

import ir.coleo.varam.R;
import ir.coleo.varam.database.models.main.Report;

public class CheckBoxManager {

    private static CheckBoxManager checkBoxManager;
    private ArrayList<CheckBoxItem> score;
    private boolean scoreModel;

    private CheckBoxManager(boolean scoreModel) {
        this.scoreModel = scoreModel;
        score = new ArrayList<>();
        if (scoreModel) {
            score.add(new CheckBoxItem(R.string.score_three_one));
            score.add(new CheckBoxItem(R.string.score_three_two));
            score.add(new CheckBoxItem(R.string.score_three_three));
            score.add(new CheckBoxItem(R.string.score_three_four));
        } else {
            score.add(new CheckBoxItem(R.string.score_four_one));
            score.add(new CheckBoxItem(R.string.score_four_two));
            score.add(new CheckBoxItem(R.string.score_four_three));
            score.add(new CheckBoxItem(R.string.score_four_four));
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j)
                    continue;
                score.get(i).add(score.get(j));
            }
        }
        score.add(new CheckBoxItem(R.string.score_zero));
        score.add(new CheckBoxItem(R.string.score_one));
        score.add(new CheckBoxItem(R.string.score_two));
        score.add(new CheckBoxItem(R.string.cartie_one));
        score.add(new CheckBoxItem(R.string.cartie_two));
        score.add(new CheckBoxItem(R.string.cartie_three));
        score.add(new CheckBoxItem(R.string.cartie_four));
        for (int i = 7; i < score.size(); i++) {
            for (int j = 7; j < score.size(); j++) {
                if (i == j)
                    continue;
                score.get(i).add(score.get(j));
            }
        }

    }

    public static CheckBoxManager getCheckBoxManager(boolean scoreModel) {
        if (checkBoxManager == null) {
            checkBoxManager = new CheckBoxManager(scoreModel);
        } else {
            if (scoreModel != checkBoxManager.scoreModel) {
                Log.i("MANAGER", "getCheckBoxManager: ");
                checkBoxManager = new CheckBoxManager(scoreModel);
            }
        }
        return checkBoxManager;
    }

    private void reset(boolean scoreModel) {
        checkBoxManager = new CheckBoxManager(scoreModel);
    }

    public boolean cartieSelected() {
        for (int i = 7; i < score.size(); i++) {
            CheckBoxItem item = score.get(i);
            if (item.isCheck() && item.isActive()) {
                return false;
            }
        }
        return true;
    }

    public boolean scoreSelected() {
        for (int i = 0; i < score.size() - 7; i++) {
            CheckBoxItem item = score.get(i);
            if (item.isCheck() && item.isActive()) {
                return false;
            }
        }
        return true;
    }

    public void setBooleansFromReport(Report report) {
        reset(report.scoreType);
        score.get(report.score).setCheck(true);
        score.get(report.score).disableOther();
        score.get(4).setCheck(report.sardalme);
        score.get(5).setCheck(report.khoni);
        score.get(6).setCheck(report.kor);

        score.get(7 + report.cartieState).setCheck(true);
        score.get(7 + report.cartieState).disableOther();
    }

    public void setBooleansOnReport(Report report) {
        for (int i = 0; i < 4; i++) {
            if (score.get(i).isCheck()) {
                report.score = i;
                break;
            }
        }
        report.sardalme = score.get(4).isCheck();
        report.khoni = score.get(5).isCheck();
        report.kor = score.get(6).isCheck();
        report.scoreType = scoreModel;
        for (int i = 7; i < score.size(); i++) {
            if (score.get(i).isCheck()) {
                report.cartieState = i - 7;
                break;
            }
        }
        reset(this.scoreModel);
    }

    public ArrayList<CheckBoxItem> getScore() {
        return score;
    }
}
