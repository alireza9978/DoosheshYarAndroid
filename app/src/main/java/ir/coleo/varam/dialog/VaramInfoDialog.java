package ir.coleo.varam.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.activities.reports.fragments.CowInjuryFragment;
import ir.coleo.varam.adapters.GridViewAdapterReasonAddReport;
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.models.CheckBoxItem;
import ir.coleo.varam.models.CheckBoxItemListener;
import ir.coleo.varam.models.CheckBoxManager;


/**
 * المان دیالوگ برای گرفتن اطلاعات جراحت پستان
 */
public class VaramInfoDialog extends Dialog {

    Context context;

    public VaramInfoDialog(@NonNull final CowInjuryFragment fragment, boolean editMode, ScoreMethod scoreMode, long lastCure) {
        super(fragment.requireContext());
        context = fragment.requireContext();
        setContentView(R.layout.select_finger_dialog_layout);

        GridView gridView = findViewById(R.id.grid);
        GridView gridViewTwo = findViewById(R.id.grid_two);
        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMode);
        CheckBoxItem checkBoxItem = manager.getCheckBoxItem(R.string.option_two);
        checkBoxItem.setValue(lastCure);
        checkBoxItem.setListener(new CheckBoxItemListener() {
            @Override
            public void run(long value) {
                if (value == -1)
                    return;
                if (value >= 14) {
                    Toast.makeText(context, R.string.recure_warning, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, R.string.near_cure_warning, Toast.LENGTH_LONG).show();
                }
            }
        });
        GridViewAdapterReasonAddReport adapter = new GridViewAdapterReasonAddReport(context, manager.getScoreTop());
        GridViewAdapterReasonAddReport adapterTwo = new GridViewAdapterReasonAddReport(context, manager.getScoreBottom());
        gridView.setAdapter(adapter);
        gridViewTwo.setAdapter(adapterTwo);

        Button newInput = findViewById(R.id.new_input);
        if (editMode) {
            newInput.setVisibility(View.GONE);
        } else {
            newInput.setOnClickListener(view -> {
                if (!isOk(manager)) {
                    return;
                }
                ((AddReportActivity) fragment.requireActivity()).addCowAndReportFast();
                dismiss();
            });
        }

        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            if (!isOk(manager)) {
                return;
            }
            dismiss();
        });
    }

    public boolean isOk(CheckBoxManager manager) {
        if (!manager.isTarkhis() && !manager.isKor()) {
            if (manager.isNew()) {
                if (!manager.scoreSelected() && !manager.isSardalme() && !manager.isKhoni()) {
                    Toast.makeText(context, R.string.new_selection_error, Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            if (!manager.cartieSelected()) {
                Toast.makeText(context, R.string.cartie_error, Toast.LENGTH_LONG).show();
                return false;
            }
            if (!manager.scoreSelected()) {
                Toast.makeText(context, R.string.score_error, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
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
}
