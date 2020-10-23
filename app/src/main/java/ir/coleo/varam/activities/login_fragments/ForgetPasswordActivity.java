package ir.coleo.varam.activities.login_fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        ImageView logo = findViewById(R.id.login_logo);
        ImageView close = findViewById(R.id.close_image);
        close.setOnClickListener(view -> finish());
        Constants.setImageBack(this, close);

        findViewById(R.id.submit).setOnClickListener(v -> {

        });


        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    if (isOpen) {
                        logo.setVisibility(View.GONE);
                        close.setVisibility(View.GONE);
                    } else {
                        logo.setVisibility(View.VISIBLE);
                        close.setVisibility(View.VISIBLE);
                    }
                });

    }
}