package ir.coleo.varam.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.activities.reports.fragments.CowInjuryFragment;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.models.CheckBoxManager;
import ir.coleo.varam.models.MyDate;


/**
 * المان دیالوگ برای گرفتن اطلاعات جراحت پستان
 */
public class VaramInfoDialog extends Dialog {

    Context context;
    private int selected = -1;
    private ImageView mainImage;
    private final int[] cartieImage = new int[]{R.drawable.ic_cartie_one, R.drawable.ic_cartie_two,
            R.drawable.ic_cartie_three, R.drawable.ic_cartie_four};

    private int cowId = -1;
    private MyDate targetDate = null;
    private boolean chronic = false;
    private boolean recurrence = false;
    private CheckBoxManager manager;


    public VaramInfoDialog(@NonNull final CowInjuryFragment fragment, boolean editMode, ScoreMethod scoreMode, int selected, MyDate targetDate, int cowId) {
        super(fragment.requireContext());
        context = fragment.requireContext();
        setContentView(R.layout.select_finger_dialog_layout);
        this.selected = selected;
        this.targetDate = targetDate;
        this.cowId = cowId;
        manager = CheckBoxManager.getCheckBoxManager(scoreMode);

        mainImage = findViewById(R.id.main_som);
        int[] buttonId = new int[]{R.id.one, R.id.two, R.id.three, R.id.four};
        for (int i = 0; i < buttonId.length; i++) {
            int finalI = i;
            findViewById(buttonId[i]).setOnClickListener(view1 -> {
                if (this.selected == -1) {
                    this.selected = finalI;
                    mainImage.setImageResource(cartieImage[finalI]);
                    lastCureError();
                } else if (this.selected == finalI) {
                    this.selected = -1;
                    mainImage.setImageResource(R.drawable.ic_area_zero);
                    Toast.makeText(context, R.string.clear_data, Toast.LENGTH_SHORT).show();
                } else {
                    errorOnlyOne();
                }
            });
        }

        if (selected == -1) {
            mainImage.setImageResource(R.drawable.ic_area_zero);
        } else {
            mainImage.setImageResource(cartieImage[selected]);
        }

        Button newInput = findViewById(R.id.new_input);
        if (editMode) {
            newInput.setVisibility(View.GONE);
        } else {
            newInput.setOnClickListener(view -> {
                if (isOk(manager)) {
                    ((AddReportActivity) fragment.requireActivity()).addCowAndReportFast();
                    dismiss();
                }
            });
        }

        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            dismiss();
        });
    }

    private boolean isOk(CheckBoxManager manager) {
        if (selected == -1) {
            Toast.makeText(context, R.string.select_cartie, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (manager.isNew()) {
            if (!manager.scoreSelected() && !manager.isSardalme() && !manager.isKhoni()) {
                Toast.makeText(context, R.string.new_selection_error, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void lastCureError() {
        if (manager.isNew()) {
            MyDao dao = DataBase.getInstance(context).dao();
            long lastCure = -1;
            if (cowId != -1) {
                ArrayList<Report> reports = (ArrayList<Report>) dao.getReportOfCowOrdered(cowId);
                if (reports.size() > 0) {
                    for (int i = reports.size() - 1; i >= 0; i--) {
                        if (reports.get(i).cartieState != null)
                            if (reports.get(i).cartieState == 1 && reports.get(i).areaNumber == selected + 1) {
                                Date startDate = reports.get(i).visit.getDate();
                                long differenceInTime = targetDate.getDate().getTime() - startDate.getTime();
                                lastCure = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
                                break;
                            }
                    }
                }
            }
            if (lastCure != -1)
                if (lastCure >= 14) {
                    recurrence = true;
                    chronic = false;
                } else {
                    recurrence = false;
                    chronic = true;
                }

        } else {
            recurrence = false;
            chronic = false;
        }
    }

    private void errorOnlyOne() {
        Toast.makeText(context, R.string.toast_select_only_one, Toast.LENGTH_SHORT).show();
    }

    public VaramInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected VaramInfoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {

    }

    public int getSelected() {
        return selected;
    }

    public boolean isChronic() {
        return chronic;
    }

    public boolean isRecurrence() {
        return recurrence;
    }
}
