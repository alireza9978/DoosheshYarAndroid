package ir.coleo.varam.dialog;

import android.app.Dialog;
import android.widget.Button;

import androidx.annotation.NonNull;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.FarmProfileActivity;


/**
 * المان دیالوگ برای گرفتن شماره انگشت
 */
public class DateModelDialog extends Dialog {


    public DateModelDialog(@NonNull final FarmProfileActivity activity) {
        super(activity);
        setContentView(R.layout.date_model_dialog_layout);

        Button single = findViewById(R.id.single_date);
        Button range = findViewById(R.id.range_date);
        Button all = findViewById(R.id.all_date);

        single.setOnClickListener(v -> {
            activity.getDate(true);
            dismiss();
        });
        range.setOnClickListener(v -> {
            activity.getDate(false);
            dismiss();
        });
        all.setOnClickListener(v -> {
            activity.export();
            dismiss();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {

    }
}
