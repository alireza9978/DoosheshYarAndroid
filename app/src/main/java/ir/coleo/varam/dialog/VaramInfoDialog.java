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
import ir.coleo.varam.models.CheckBoxManager;


/**
 * المان دیالوگ برای گرفتن اطلاعات جراحت پستان
 */
public class VaramInfoDialog extends Dialog {


    public VaramInfoDialog(@NonNull final CowInjuryFragment fragment, boolean editMode, ScoreMethod scoreMode) {
        super(fragment.requireContext());
        Context context = fragment.requireContext();
        setContentView(R.layout.select_finger_dialog_layout);

        GridView gridView = findViewById(R.id.grid);
        GridView gridViewTwo = findViewById(R.id.grid_two);
        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMode);
        GridViewAdapterReasonAddReport adapter = new GridViewAdapterReasonAddReport(context, manager.getScoreTop());
        GridViewAdapterReasonAddReport adapterTwo = new GridViewAdapterReasonAddReport(context, manager.getScoreBottom());
        gridView.setAdapter(adapter);
        gridViewTwo.setAdapter(adapterTwo);

        Button newInput = findViewById(R.id.new_input);
        if (editMode) {
            newInput.setVisibility(View.GONE);
        } else {
            newInput.setOnClickListener(view -> {
                if (!manager.isTarkhis()) {
                    if (!manager.cartieSelected()) {
                        Toast.makeText(context, R.string.cartie_error, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!manager.scoreSelected()) {
                        Toast.makeText(context, R.string.score_error, Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                ((AddReportActivity) fragment.requireActivity()).addCowAndReportFast();
                dismiss();
            });
        }

        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(v -> {
            if (!manager.isTarkhis() && !manager.isKor()) {
                if (!manager.cartieSelected()) {
                    Toast.makeText(context, R.string.cartie_error, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!manager.scoreSelected()) {
                    Toast.makeText(context, R.string.score_error, Toast.LENGTH_LONG).show();
                    return;
                }
            }
            dismiss();
        });
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
