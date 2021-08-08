package ir.coleo.varam.models;

import android.util.Log;

import java.util.ArrayList;

import ir.coleo.varam.R;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.models.main.ScoreMethod;

/**
 * مدیریت کننده اطلاعات مربوط به اپشن‌های ورودی در هنگام ثبت گزارش
 */
public class CheckBoxManager {

    private static CheckBoxManager checkBoxManager;
    private final ArrayList<CheckBoxItem> score;
    private final ScoreMethod scoreMethod;

    private CheckBoxManager(ScoreMethod scoreMethod) {
        this.scoreMethod = scoreMethod;
        score = new ArrayList<>();
        for (String name : scoreMethod.scoresNameList) {
            score.add(new CheckBoxItem(name));
        }

        for (int i = 0; i < scoreMethod.scoresCount; i++) {
            for (int j = 0; j < scoreMethod.scoresCount; j++) {
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

        score.get(scoreMethod.scoresCount + 2).add(score.get(scoreMethod.scoresCount));
        score.get(scoreMethod.scoresCount + 2).add(score.get(scoreMethod.scoresCount + 1));
        score.get(scoreMethod.scoresCount + 2).add(score.get(scoreMethod.scoresCount + 3));
        score.get(scoreMethod.scoresCount + 2).add(score.get(scoreMethod.scoresCount + 4));
        score.get(scoreMethod.scoresCount + 2).add(score.get(scoreMethod.scoresCount + 5));
        score.get(scoreMethod.scoresCount + 2).add(score.get(scoreMethod.scoresCount + 6));

        for (int i = scoreMethod.scoresCount + 3; i < score.size(); i++) {
            for (int j = scoreMethod.scoresCount + 3; j < score.size(); j++) {
                if (i == j)
                    continue;
                score.get(i).add(score.get(j));
            }
        }

    }

    public static CheckBoxManager getCheckBoxManager(ScoreMethod scoreMethod) {
        if (checkBoxManager == null) {
            checkBoxManager = new CheckBoxManager(scoreMethod);
        } else {
            if (scoreMethod != checkBoxManager.scoreMethod) {
                Log.i("MANAGER", "getCheckBoxManager: ");
                checkBoxManager = new CheckBoxManager(scoreMethod);
            }
        }
        return checkBoxManager;
    }

    private void reset(ScoreMethod scoreMethod) {
        checkBoxManager = new CheckBoxManager(scoreMethod);
    }

    public boolean isTarkhis() {
        return score.get(scoreMethod.scoresCount + 6).isCheck();
    }

    public boolean isKor() {
        return score.get(scoreMethod.scoresCount + 2).isCheck();
    }

    public boolean cartieSelected() {
        for (int i = scoreMethod.scoresCount + 3; i < score.size(); i++) {
            CheckBoxItem item = score.get(i);
            if (item.isCheck() && item.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean scoreSelected() {
        for (int i = 0; i < scoreMethod.scoresCount; i++) {
            CheckBoxItem item = score.get(i);
            if (item.isCheck() && item.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void setBooleansFromReport(Report report, ScoreMethod scoreMethod) {
        reset(scoreMethod);
        checkBoxManager.score.get(report.score).setCheck(true);
        checkBoxManager.score.get(report.score).disableOther();
        checkBoxManager.score.get(scoreMethod.scoresCount).setCheck(report.sardalme);
        checkBoxManager.score.get(scoreMethod.scoresCount + 1).setCheck(report.khoni);
        checkBoxManager.score.get(scoreMethod.scoresCount + 2).setCheck(report.kor);

        checkBoxManager.score.get(scoreMethod.scoresCount + 3 + report.cartieState).setCheck(true);
        checkBoxManager.score.get(scoreMethod.scoresCount + 3 + report.cartieState).disableOther();
    }

    public void setBooleansOnReport(Report report) {
        for (int i = 0; i < scoreMethod.scoresCount; i++) {
            if (score.get(i).isCheck()) {
                report.score = i;
                break;
            }
        }
        report.sardalme = score.get(scoreMethod.scoresCount).isCheck();
        report.khoni = score.get(scoreMethod.scoresCount + 1).isCheck();
        report.kor = score.get(scoreMethod.scoresCount + 2).isCheck();
        report.scoreMethodId = scoreMethod.id;
        for (int i = scoreMethod.scoresCount + 3; i < score.size(); i++) {
            if (score.get(i).isCheck()) {
                report.cartieState = i - scoreMethod.scoresCount - 3;
                break;
            }
        }
        reset(this.scoreMethod);
    }

    public ArrayList<CheckBoxItem> getScore() {
        return score;
    }

}
