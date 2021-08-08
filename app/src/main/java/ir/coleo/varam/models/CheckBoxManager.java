package ir.coleo.varam.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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

        Integer[] scoresName = {R.string.option_five, R.string.option_six, R.string.option_two,
                R.string.option_three, R.string.option_eight, R.string.option_four,
                R.string.option_one, R.string.option_seven};

        for (Integer integer : scoresName) {
            score.add(new CheckBoxItem(integer));
        }


        disableOthers(R.string.option_one, new ArrayList<>(Arrays.asList(R.string.option_two, R.string.option_three, R.string.option_eight, R.string.option_four, R.string.option_seven)));
        disableOthers(R.string.option_two, new ArrayList<>(Arrays.asList(R.string.option_three, R.string.option_eight, R.string.option_four, R.string.option_one, R.string.option_seven)));
        disableOthers(R.string.option_three, new ArrayList<>(Arrays.asList(R.string.option_two, R.string.option_eight, R.string.option_four, R.string.option_one, R.string.option_seven)));
        disableOthers(R.string.option_four, new ArrayList<>(Arrays.asList(R.string.option_two, R.string.option_three, R.string.option_eight, R.string.option_one, R.string.option_seven)));
        disableOthers(R.string.option_seven, new ArrayList<>(Arrays.asList(R.string.option_five, R.string.option_six, R.string.option_two, R.string.option_three, R.string.option_eight, R.string.option_four, R.string.option_one)));
        disableOthers(R.string.option_eight, new ArrayList<>(Arrays.asList(R.string.option_two, R.string.option_three, R.string.option_four, R.string.option_one, R.string.option_seven)));

    }

    private void disableOthers(Integer main, ArrayList<Integer> other) {
        for (CheckBoxItem item : score) {
            Integer mainName = item.getName();
            if (mainName != null && mainName.equals(main)) {
                for (CheckBoxItem otherItem : score) {
                    Integer otherName = item.getName();
                    if (otherName != null)
                        for (Integer otherInteger : other) {
                            if (otherName.equals(otherInteger)) {
                                item.add(otherItem);
                            }
                        }
                }
                return;
            }

        }
    }

    private CheckBoxItem getByName(Integer name) {
        for (CheckBoxItem item : score) {
            Integer mainName = item.getName();
            if (mainName != null && mainName.equals(name)) {
                return item;
            }
        }
        return null;
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
        return Objects.requireNonNull(getByName(R.string.option_four)).isCheck();
    }

    public boolean isKor() {
        return Objects.requireNonNull(getByName(R.string.option_seven)).isCheck();
    }

    public boolean cartieSelected() {
        Integer[] scoresName = {R.string.option_five, R.string.option_six, R.string.option_seven};
        for (Integer integer : scoresName) {
            CheckBoxItem item = getByName(integer);
            if (item != null && item.isCheck() && item.isActive()) {
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
        Objects.requireNonNull(checkBoxManager.getByName(R.string.option_five)).setCheck(report.sardalme);
        Objects.requireNonNull(checkBoxManager.getByName(R.string.option_six)).setCheck(report.khoni);
        Objects.requireNonNull(checkBoxManager.getByName(R.string.option_seven)).setCheck(report.kor);

        Integer[] scoresName = {R.string.option_two, R.string.option_three, R.string.option_eight, R.string.option_four, R.string.option_one};
        Objects.requireNonNull(checkBoxManager.getByName(scoresName[report.cartieState])).setCheck(true);
        Objects.requireNonNull(checkBoxManager.getByName(scoresName[report.cartieState])).disableOther();

    }

    public void setBooleansOnReport(Report report) {
        for (int i = 0; i < scoreMethod.scoresCount; i++) {
            if (score.get(i).isCheck()) {
                report.score = i;
                break;
            }
        }
        report.sardalme = Objects.requireNonNull(checkBoxManager.getByName(R.string.option_five)).isCheck();
        report.khoni = Objects.requireNonNull(checkBoxManager.getByName(R.string.option_six)).isCheck();
        report.kor = Objects.requireNonNull(checkBoxManager.getByName(R.string.option_seven)).isCheck();
        report.scoreMethodId = scoreMethod.id;

        Integer[] scoresName = {R.string.option_two, R.string.option_three, R.string.option_eight, R.string.option_four, R.string.option_one};

        for (int i = 0; i < scoresName.length; i++) {
            if (Objects.requireNonNull(getByName(scoresName[i])).isCheck()) {
                report.cartieState = i;
                break;
            }
        }
        reset(this.scoreMethod);
    }

    public ArrayList<CheckBoxItem> getScore() {
        return score;
    }

    public ArrayList<CheckBoxItem> getScoreTop() {
        ArrayList<CheckBoxItem> checkBoxManagers = new ArrayList<>();
        for (int i = 0; i < scoreMethod.scoresCount; i++) {
            checkBoxManagers.add(score.get(i));
        }
        return checkBoxManagers;
    }

    public ArrayList<CheckBoxItem> getScoreBottom() {
        ArrayList<CheckBoxItem> checkBoxManagers = new ArrayList<>();
        for (int i = scoreMethod.scoresCount; i < score.size(); i++) {
            checkBoxManagers.add(score.get(i));
        }
        return checkBoxManagers;
    }

}
