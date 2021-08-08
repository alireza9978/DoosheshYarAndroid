package ir.coleo.varam.activities.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.MoreInfoActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.main.ScoreMethod;

public class CreateScoreMethod extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_score_method);

        findViewById(R.id.close_image).setOnClickListener(v -> finish());
        LinearLayout scoresList = findViewById(R.id.scores_list_id);
        ArrayList<EditText> editTexts = new ArrayList<>();
        findViewById(R.id.info_image).setOnClickListener(v -> {
            Intent intent = new Intent(this, MoreInfoActivity.class);
            startActivity(intent);
        });

        Intent data = getIntent();
        assert data != null;
        String mode = data.getStringExtra(Constants.SCORE_METHOD_INTENT_MODE);
        if(mode.equals("CREATE")) {

            int scoreCount = data.getIntExtra(Constants.SCORE_METHOD_INTENT_COUNT, 0);
            if (scoreCount == 0) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }

            for (int i = 0; i < scoreCount; i++) {
                View child = getLayoutInflater().inflate(R.layout.score_list_item_layout, null);
                EditText tempEditText = child.findViewById(R.id.item_name);
                TextView tempTextView = child.findViewById(R.id.item_text);
                tempTextView.setText(getString(R.string.level) + (i + 1) );
                editTexts.add(tempEditText);
                scoresList.addView(child);
            }

            findViewById(R.id.submit).setOnClickListener(v -> {
                List<String> scoresNameList = new ArrayList<>();
                for (EditText editText : editTexts) {
                    String temp = editText.getText().toString();
                    if (temp.isEmpty()) {
                        Toast.makeText(CreateScoreMethod.this, getString(R.string.invalid_input), Toast.LENGTH_LONG).show();
                        return;
                    }
                    scoresNameList.add(temp);
                }
                ScoreMethod scoreMethod = new ScoreMethod();
                scoreMethod.scoresCount = scoreCount;
                scoreMethod.scoresNameList = scoresNameList;
                Intent intent = new Intent();
                intent.putExtra(Constants.SCORE_METHOD_INTENT, scoreMethod);
                setResult(Activity.RESULT_OK, intent);
                finish();
            });
        }else if (mode.equals("IMPORT")){
            ArrayList<String> scoresName = data.getStringArrayListExtra(Constants.SCORE_METHOD_INTENT_DATA);
            if (scoresName == null || scoresName.size() == 0) {
                setResult(Activity.RESULT_CANCELED);
                finish();
                return;
            }
            int scoreCount = scoresName.size();

            for (int i = 0; i < scoreCount; i++) {
                View child = getLayoutInflater().inflate(R.layout.score_list_item_layout, scoresList);
                EditText tempEditText = child.findViewById(R.id.item_name);
                TextView tempTextView = child.findViewById(R.id.item_text);
                tempTextView.setText(scoresName.get(i));
                editTexts.add(tempEditText);
                scoresList.addView(child);
            }

            findViewById(R.id.submit).setOnClickListener(v -> {
                String[] scoresNameList = new String[scoreCount];
                for (int i = 0; i < editTexts.size(); i++) {
                    EditText editText = editTexts.get(i);
                    String temp = editText.getText().toString();
                    if (temp.isEmpty()) {
                        Toast.makeText(CreateScoreMethod.this, getString(R.string.invalid_input), Toast.LENGTH_LONG).show();
                        return;
                    }
                    int position = Integer.parseInt(temp);
                    if (position < scoreCount) {
                        scoresNameList[position] = scoresName.get(i);
                    } else {
                        Toast.makeText(CreateScoreMethod.this, getString(R.string.invalid_input_big_number), Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                ScoreMethod scoreMethod = new ScoreMethod();
                scoreMethod.scoresCount = scoreCount;
                scoreMethod.scoresNameList = new ArrayList<>(Arrays.asList(scoresNameList));
                Intent intent = new Intent();
                intent.putExtra(Constants.SCORE_METHOD_INTENT, scoreMethod);
                setResult(Activity.RESULT_OK, intent);
                finish();
            });

        }

    }
}