package ir.coleo.varam.activities.reports;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;

/**
 * صفحه اطلاعات که در مراحل ثبت گزارش قابل دسترس است
 * با توجه به ورود داده نمایش داده شده متفاوت است
 */
public class MoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        findViewById(R.id.close_image).setOnClickListener(view -> finish());
        Constants.setImageBack(this, findViewById(R.id.close_image));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            State state = (State) bundle.get(Constants.MORE_INFO_STATE);

            if (state != null)
                switch (state) {
                    case info:
                        findViewById(R.id.one).setVisibility(View.VISIBLE);
                        break;
                    case injury:
                        findViewById(R.id.two).setVisibility(View.VISIBLE);
                        break;
                    case drugs:
                        findViewById(R.id.three).setVisibility(View.VISIBLE);
                        break;
                    case moreInfo:
                        findViewById(R.id.four).setVisibility(View.VISIBLE);
                        break;
                }
        } else {
            findViewById(R.id.createMethod).setVisibility(View.VISIBLE);
        }
    }
}