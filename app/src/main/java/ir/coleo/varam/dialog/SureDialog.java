package ir.coleo.varam.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ir.coleo.varam.R;


/**
 * المان پرسش مجدد برای خروج یا حذف آدرس
 */
public class SureDialog extends Dialog {

    public SureDialog(@NonNull Context context, String question, String text,
                      final Runnable yes, final Runnable no,
                      String yesText, String noText) {
        super(context);
        setContentView(R.layout.temp_dialog_layout);

        ((TextView) findViewById(R.id.mainTitleId)).setText(question);
        ((TextView) findViewById(R.id.dialogDescriptionId)).setText(text);
        ((TextView) findViewById(R.id.yesDialogBtn)).setText(yesText);
        if (yes == null) {
            findViewById(R.id.noDialogBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            findViewById(R.id.yesDialogBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    yes.run();
                    dismiss();
                }
            });
        }
        ((TextView) findViewById(R.id.noDialogBtn)).setText(noText);
        if (no != null) {
            findViewById(R.id.noDialogBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    no.run();
                    dismiss();
                }
            });
        } else {
            findViewById(R.id.noDialogBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }


    }

}

