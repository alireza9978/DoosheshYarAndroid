package ir.coleo.varam.activities.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.MoreInfoActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.main.ScoreMethod;

public class CreateScoreMethod extends AppCompatActivity {

    private int scoreCount = 3;
    private final Stack<View> views = new Stack<>();
    private final Stack<EditText> editTexts = new Stack<>();
    private LinearLayout scoresList;

    private void addViewToList(String name, int i, boolean lastOne, boolean combine) {
        View child = getLayoutInflater().inflate(R.layout.score_list_item_layout, null);
        EditText tempEditText = child.findViewById(R.id.item_name);
        TextView tempTextView = child.findViewById(R.id.item_text);
        if(combine){
            tempTextView.setText(name + (i + 1));
        }else{
            tempTextView.setText(name);
        }

        editTexts.add(tempEditText);
        views.push(child);
        scoresList.addView(child);

        if (!views.isEmpty()) {
            EditText last = views.peek().findViewById(R.id.item_name);
            last.setNextFocusDownId(tempEditText.getId());
            last.setNextFocusUpId(tempEditText.getId());
            last.setNextFocusLeftId(tempEditText.getId());
            last.setNextFocusRightId(tempEditText.getId());
        }
        if (lastOne){
            tempEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_score_method);


        scoresList = findViewById(R.id.scores_list_id);
        findViewById(R.id.close_image).setOnClickListener(v -> finish());
        findViewById(R.id.info_image).setOnClickListener(v -> {
            Intent intent = new Intent(this, MoreInfoActivity.class);
            startActivity(intent);
        });


        Intent data = getIntent();
        assert data != null;
        String mode = data.getStringExtra(Constants.SCORE_METHOD_INTENT_MODE);
        if (mode.equals("CREATE")) {

            findViewById(R.id.add).setOnClickListener(v -> {
                if (scoreCount < 5) {
                    scoreCount++;
                    EditText beforeLast = editTexts.peek();
                    beforeLast.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    addViewToList(getString(R.string.level), scoreCount - 1, true,true);
                    if (!views.isEmpty()) {
                        EditText last = editTexts.peek();
                        beforeLast.setNextFocusDownId(last.getId());
                        beforeLast.setNextFocusUpId(last.getId());
                        beforeLast.setNextFocusLeftId(last.getId());
                        beforeLast.setNextFocusRightId(last.getId());
                    }
                } else {
                    Toast.makeText(CreateScoreMethod.this, R.string.max_level_reaches, Toast.LENGTH_SHORT).show();
                }
            });
            findViewById(R.id.sub).setOnClickListener(v -> {
                if (scoreCount > 3) {
                    scoreCount--;
                    View child = views.pop();
                    editTexts.pop();
                    scoresList.removeView(child);

                    editTexts.peek().setImeOptions(EditorInfo.IME_ACTION_DONE);

                } else {
                    Toast.makeText(CreateScoreMethod.this, R.string.min_level_reaches, Toast.LENGTH_SHORT).show();
                }
            });


            for (int i = 0; i < scoreCount; i++) {
                addViewToList(getString(R.string.level), i, i == scoreCount - 1,true);
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
        } else if (mode.equals("IMPORT")) {

            findViewById(R.id.sub).setVisibility(View.GONE);
            findViewById(R.id.add).setVisibility(View.GONE);

            ArrayList<String> scoresName = data.getStringArrayListExtra(Constants.SCORE_METHOD_INTENT_DATA);
            if (scoresName == null || scoresName.size() == 0) {
                setResult(Activity.RESULT_CANCELED);
                finish();
                return;
            }
            this.scoreCount = scoresName.size();

            for (int i = 0; i < scoreCount; i++) {
                addViewToList(scoresName.get(i), i, i == scoreCount - 1,false);
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
                    if (position <= 0) {
                        Toast.makeText(CreateScoreMethod.this, getString(R.string.invalid_input_small_number), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (position <= scoreCount) {
                        scoresNameList[position - 1] = scoresName.get(i);
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