package ir.coleo.varam.activities.menu;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;

import mehdi.sakout.fancybuttons.FancyButton;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ImageView back = findViewById(R.id.back_image);
        Constants.setImageBack(this, back);
        back.setOnClickListener(view -> finish());

        EditText username = findViewById(R.id.user_name_input);
        EditText email = findViewById(R.id.email_input);
        EditText oldPass = findViewById(R.id.old_password_input);
        EditText newPass = findViewById(R.id.new_password_input);
        EditText confirmPass = findViewById(R.id.new_password_input_confirm);
        FancyButton submit = findViewById(R.id.submit);
        submit.setOnClickListener(view -> finish());

    }
}