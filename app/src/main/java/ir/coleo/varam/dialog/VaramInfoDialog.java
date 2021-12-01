package ir.coleo.varam.dialog;

import static com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.activities.reports.fragments.CowInjuryFragment;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.CheckBoxManager;
import ir.coleo.varam.models.MyDate;


/**
 * المان دیالوگ برای گرفتن اطلاعات جراحت پستان
 */
public class VaramInfoDialog extends Dialog {

    Context context;
    private int selected = -1;
    private ImageView mainImage;
    private final int[] cartieImage = new int[]{R.drawable.ic_cartie_one, R.drawable.ic_cartie_two,
            R.drawable.ic_cartie_three, R.drawable.ic_cartie_four};

    private int cowId = -1;
    private MyDate targetDate = null;
    private boolean chronic = false;
    private boolean recurrence = false;
    private ScoreMethod scoreMethod;

    private boolean fastDone = false;

    public VaramInfoDialog(@NonNull final CowInjuryFragment fragment, boolean editMode, ScoreMethod scoreMode, int selected, MyDate targetDate, int cowId) {
        super(fragment.requireContext());
        context = fragment.requireContext();
        setContentView(R.layout.select_finger_dialog_layout);
        this.selected = selected;
        this.targetDate = targetDate;
        this.cowId = cowId;
        this.scoreMethod = scoreMode;

        mainImage = findViewById(R.id.main_som);
        int[] buttonId = new int[]{R.id.one, R.id.two, R.id.three, R.id.four};
        for (int i = 0; i < buttonId.length; i++) {
            int finalI = i;
            findViewById(buttonId[i]).setOnClickListener(view1 -> {
                if (this.selected == -1) {
                    this.selected = finalI;
                    mainImage.setImageResource(cartieImage[finalI]);
                    lastCureError();
                } else if (this.selected == finalI) {
                    this.selected = -1;
                    mainImage.setImageResource(R.drawable.ic_area_zero);
                    Toast.makeText(context, R.string.clear_data, Toast.LENGTH_SHORT).show();
                } else {
                    errorOnlyOne();
                }
            });
        }

        if (selected == -1) {
            mainImage.setImageResource(R.drawable.ic_area_zero);
        } else {
            mainImage.setImageResource(cartieImage[selected]);
        }

        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(this.scoreMethod);
        Button newInput = findViewById(R.id.new_input);
        if (editMode) {
            newInput.setVisibility(View.GONE);
        } else {
//            if (manager.isNew() || manager.isCureChange()) {
//                newInput.setVisibility(View.GONE);
//            } else {
//
//            }
            newInput.setVisibility(View.VISIBLE);
            newInput.setOnClickListener(view -> {
                if (isOk()) {
                    fragment.setSelected(this.selected);
                    fragment.setChronic(chronic);
                    fragment.setRecurrence(recurrence);
                    fastDone = true;
                    ((AddReportActivity) fragment.requireActivity()).addCowAndReportFast();
                    dismiss();
                }
            });
        }

        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(v -> dismiss());
    }

    public boolean isFastDone() {
        return fastDone;
    }

    private boolean isOk() {
        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(this.scoreMethod);
        if (selected == -1) {
            Toast.makeText(context, R.string.select_cartie, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (manager.isNew()) {
            if (!manager.scoreSelected() && !manager.isSardalme() && !manager.isKhoni()) {
                Toast.makeText(context, R.string.new_selection_error, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (!manager.isSelectionOk()) {
            Toast.makeText(context, R.string.empty_error, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void lastCureError() {
        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(this.scoreMethod);
        if (manager.isNew()) {
            MyDao dao = DataBase.getInstance(context).dao();
            AtomicLong lastCure = new AtomicLong(-1L);
            if (cowId != -1) {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    ArrayList<Report> reports = (ArrayList<Report>) dao.getReportOfCowOrdered(cowId);
                    if (reports.size() > 0) {
                        for (int i = reports.size() - 1; i >= 0; i--) {
                            if (reports.get(i).cartieState != null)
                                if (reports.get(i).cartieState == 1 && reports.get(i).areaNumber == selected + 1) {
                                    Date startDate = reports.get(i).visit.getDate();
                                    long differenceInTime = targetDate.getDate().getTime() - startDate.getTime();
                                    lastCure.set((differenceInTime / (1000 * 60 * 60 * 24)) % 365);
                                    break;
                                }
                        }
                    }
                    runOnUiThread(() -> {
                        if (lastCure.get() == -1)
                            return;
                        if (lastCure.get() >= 14) {
                            Toast.makeText(context, R.string.recure_warning, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, R.string.near_cure_warning, Toast.LENGTH_LONG).show();
                        }
                    });
                    if (lastCure.get() != -1)
                        if (lastCure.get() >= 14) {
                            recurrence = true;
                            chronic = false;
                        } else {
                            recurrence = false;
                            chronic = true;
                        }

                });

            }
        } else {
            recurrence = false;
            chronic = false;
        }
    }

    private void errorOnlyOne() {
        Toast.makeText(context, R.string.toast_select_only_one, Toast.LENGTH_SHORT).show();
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

    public int getSelected() {
        return selected;
    }

    public boolean isChronic() {
        return chronic;
    }

    public boolean isRecurrence() {
        return recurrence;
    }
}
