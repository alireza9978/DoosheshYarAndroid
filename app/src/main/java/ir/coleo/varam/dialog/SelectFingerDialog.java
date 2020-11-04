package ir.coleo.varam.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.AddReportActivity;


/**
 * المان دیالوگ برای گرفتن شماره انگشت
 */
public class SelectFingerDialog extends Dialog {

    String TAG = "ErrorDialog";

    public SelectFingerDialog(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.select_finger_dialog_layout);

    }

    public SelectFingerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SelectFingerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    public void onBackPressed() {

    }
}
