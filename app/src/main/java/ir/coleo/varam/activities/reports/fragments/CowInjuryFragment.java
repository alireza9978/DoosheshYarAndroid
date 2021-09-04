package ir.coleo.varam.activities.reports.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.dialog.VaramInfoDialog;
import ir.coleo.varam.models.CheckBoxManager;
import ir.coleo.varam.models.MyDate;

/**
 * صفحه صبت جراحت پستان در ثبت گزارش
 */
public class CowInjuryFragment extends Fragment {

    private int selected = -1;
    private ImageView mainImage;
    private final int[] buttonId = new int[]{R.id.one, R.id.two, R.id.three, R.id.four};
    private final int[] cartieImage = new int[]{R.drawable.ic_cartie_one, R.drawable.ic_cartie_two,
            R.drawable.ic_cartie_three, R.drawable.ic_cartie_four};
    private final ScoreMethod scoreMethod;
    private boolean edit = false;

    private int cowId = -1;
    private MyDate targetDate = null;
    private long lastCure = -1;
    private boolean chronic = false;
    private boolean recurrence = false;

    public CowInjuryFragment(int selected, ScoreMethod scoreMethod, int cowId) {
        this.edit = true;
        this.selected = selected;
        this.scoreMethod = scoreMethod;
        this.cowId = cowId;
    }

    public CowInjuryFragment(ScoreMethod scoreMethod, int cowId) {
        this.scoreMethod = scoreMethod;
        this.cowId = cowId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cow_injury, container, false);
        Constants.setImageFront(requireContext(), view.findViewById(R.id.next_icon));
        Constants.setImageBack(requireContext(), view.findViewById(R.id.back_icon));
        mainImage = view.findViewById(R.id.main_som);
        for (int i = 0; i < buttonId.length; i++) {
            int finalI = i;
            view.findViewById(buttonId[i]).setOnClickListener(view1 -> {
                if (selected == -1) {
                    selected = finalI;
                    mainImage.setImageResource(cartieImage[finalI]);
                    getFingerNumber();
                } else if (selected == finalI) {
                    selected = -1;
                    mainImage.setImageResource(R.drawable.ic_area_zero);
                    Toast.makeText(requireContext(), R.string.clear_data, Toast.LENGTH_SHORT).show();
                } else {
                    errorOnlyOne();
                }
            });
        }

        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            if (selected == -1) {
                Toast.makeText(requireContext(), R.string.empty_error, Toast.LENGTH_SHORT).show();
                return;
            }
            CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMethod);
            if (manager.isNew()) {
                if (lastCure != -1)
                    if (lastCure >= 14) {
                        recurrence = true;
                        chronic = false;
                    } else {
                        recurrence = false;
                        chronic = true;
                    }
            } else {
                recurrence = false;
                chronic = false;
            }
            ((AddReportActivity) requireActivity()).next();
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            ((AddReportActivity) requireActivity()).back();
        });
        return view;
    }

    public void getFingerNumber() {
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            lastCure = -1;
            if (cowId != -1) {
                ArrayList<Report> reports = (ArrayList<Report>) dao.getReportOfCowOrdered(cowId);
                if (reports.size() == 0) {
                    lastCure = -1;
                } else {
                    for (int i = reports.size() - 1; i >= 0; i--) {
                        if (reports.get(i).cartieState != null)
                            if (reports.get(i).cartieState == 1 && reports.get(i).areaNumber == selected + 1) {
                                Date startDate = reports.get(i).visit.getDate();
                                long differenceInTime = targetDate.getDate().getTime() - startDate.getTime();
                                lastCure = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
                                break;
                            }
                    }
                }
            }
            requireActivity().runOnUiThread(() -> {
                Log.i("TAG", "getFingerNumber: " + lastCure);
                VaramInfoDialog dialog = new VaramInfoDialog(this, edit, scoreMethod, lastCure);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setOnDismissListener(dialogInterface -> {
                    CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMethod);
                    if (!manager.isSelectionOk()) {
                        reset();
                    }
                    ((AddReportActivity) requireActivity()).hideKeyboard();
                });
                dialog.show();
            });
        });


    }

    public void reset() {
        selected = -1;
        mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_area_zero));
    }

    private void errorOnlyOne() {
        Toast.makeText(requireContext(), R.string.toast_select_only_one, Toast.LENGTH_SHORT).show();
    }

    public int getSelected() {
        return selected;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (selected == -1) {
            mainImage.setImageResource(R.drawable.ic_area_zero);
        } else {
            mainImage.setImageResource(cartieImage[selected]);
        }
    }

    public void setTargetDate(MyDate targetDate) {
        this.targetDate = targetDate;
    }

    public Boolean isChronic() {
        return chronic;
    }

    public boolean isRecurrence() {
        return recurrence;
    }

    public void setCowId(int cowId) {
        this.cowId = cowId;
    }
}