package ir.coleo.varam.models;

import java.util.ArrayList;

import ir.coleo.varam.R;
import ir.coleo.varam.database.models.main.Report;

public class CheckBoxManager {

    private static CheckBoxManager checkBoxManager;
    private ArrayList<CheckBoxItem> cartie;
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

        cartie = new ArrayList<>();
        cartie.add(new CheckBoxItem(R.string.cartie_one));
        cartie.add(new CheckBoxItem(R.string.cartie_two));
        cartie.add(new CheckBoxItem(R.string.cartie_three));
        cartie.add(new CheckBoxItem(R.string.cartie_four));

    }

    public static CheckBoxManager getCheckBoxManager(boolean scoreModel) {
        if (checkBoxManager == null) {
            checkBoxManager = new CheckBoxManager(scoreModel);
        } else {
            if (scoreModel != checkBoxManager.scoreModel) {
                checkBoxManager = new CheckBoxManager(scoreModel);
            }
        }
        return checkBoxManager;
    }

    private void reset(boolean scoreModel) {
        if (this.scoreModel == scoreModel) {
            for (CheckBoxItem item : score) {
                item.setCheck(false);
                item.setActive(true);
            }
        } else {
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

        }
        for (CheckBoxItem item : cartie) {
            item.setCheck(false);
            item.setActive(true);
        }
    }

    public boolean cartieSelected() {
        for (CheckBoxItem item : cartie) {
            if (item.isCheck() && item.isActive()) {
                return true;
            }
        }
        return false;
    }

    public boolean scoreSelected() {
        for (CheckBoxItem item : score) {
            if (item.isCheck() && item.isActive()) {
                return true;
            }
        }
        return false;
    }

    public void setBooleansFromReport(Report report) {
        reset(report.scoreType);
        score.get(report.score).setCheck(true);
        score.get(report.score).disableOther();
        score.get(4).setCheck(report.sardalme);
        score.get(5).setCheck(report.khoni);
        score.get(6).setCheck(report.kor);

        cartie.get(report.cartieState).setCheck(true);
        cartie.get(report.cartieState).disableOther();
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
        for (int i = 0; i < cartie.size(); i++) {
            if (cartie.get(i).isCheck()) {
                report.cartieState = i;
                break;
            }
        }
        reset(this.scoreModel);
    }

    public ArrayList<CheckBoxItem> getCartie() {
        return cartie;
    }

    public ArrayList<CheckBoxItem> getScore() {
        return score;
    }
}
