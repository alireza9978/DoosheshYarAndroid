package ir.coleo.varam.activities.reports.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.adapters.GridViewAdapterReasonAddReport;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.dialog.VaramInfoDialog;
import ir.coleo.varam.models.CheckBoxItem;
import ir.coleo.varam.models.CheckBoxItemListener;
import ir.coleo.varam.models.CheckBoxManager;
import ir.coleo.varam.models.MyDate;

/**
 * صفحه ثبت جراحت پستان در ثبت گزارش
 */
public class CowInjuryFragment extends Fragment {

    private int selected = -1;
    private final ScoreMethod scoreMethod;
    private boolean edit = false;

    private int cowId;
    private MyDate targetDate = null;
    private boolean chronic = false;
    private boolean recurrence = false;
    private GridViewAdapterReasonAddReport adapter, adapterTwo;
    private GridView gridView, gridViewTwo;
    private Button cartieSelection;

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

        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMethod);
        gridView = view.findViewById(R.id.grid);
        gridViewTwo = view.findViewById(R.id.grid_two);
        CheckBoxItem checkBoxItem = manager.getCheckBoxItem(R.string.option_two);
        checkBoxItem.setListener(new CheckBoxItemListener() {
            @Override
            public void run() {
                if (selected != -1) {
                    getLastCureError();
                }
            }
        });

        adapter = new GridViewAdapterReasonAddReport(requireContext(), manager.getScoreTop());
        adapterTwo = new GridViewAdapterReasonAddReport(requireContext(), manager.getScoreBottom());
        gridView.setAdapter(adapter);
        gridViewTwo.setAdapter(adapterTwo);
        cartieSelection = view.findViewById(R.id.cartie_selection);
        cartieSelection.setOnClickListener(v -> {
            getCartieNumber();
        });
        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            if (isOk()) {
                ((AddReportActivity) requireActivity()).next();
            }
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            ((AddReportActivity) requireActivity()).back();
        });
        return view;
    }

    private void getLastCureError() {
        MyDao dao = DataBase.getInstance(requireContext()).dao();
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
                requireActivity().runOnUiThread(() -> {
                    if (lastCure.get() == -1)
                        return;
                    if (lastCure.get() >= 14) {
                        Toast.makeText(requireContext(), R.string.recure_warning, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(requireContext(), R.string.near_cure_warning, Toast.LENGTH_LONG).show();
                    }
                });
            });
        }
    }

    public void getCartieNumber() {
        VaramInfoDialog dialog = new VaramInfoDialog(this, edit, scoreMethod, selected, targetDate, cowId);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setOnDismissListener(dialogInterface -> {
            if (!dialog.isFastDone()){
                selected = dialog.getSelected();
                chronic = dialog.isChronic();
                recurrence = dialog.isRecurrence();
                ((AddReportActivity) requireActivity()).hideKeyboard();
                switch (selected) {
                    case 0: {
                        cartieSelection.setText(R.string.cartie_selection_text_one);
                        break;
                    }
                    case 1: {
                        cartieSelection.setText(R.string.cartie_selection_text_two);
                        break;
                    }
                    case 2: {
                        cartieSelection.setText(R.string.cartie_selection_text_three);
                        break;
                    }
                    case 3: {
                        cartieSelection.setText(R.string.cartie_selection_text_four);
                        break;
                    }
                    default:
                        cartieSelection.setText(R.string.cartie_selection_text);
                }
            }
        });
        dialog.show();
    }

    public void setChronic(boolean chronic) {
        this.chronic = chronic;
    }

    public void setRecurrence(boolean recurrence) {
        this.recurrence = recurrence;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return selected;
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

    public boolean isOk() {
        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMethod);
        if (selected == -1) {
            Toast.makeText(requireContext(), R.string.select_cartie, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (manager.isNew()) {
            if (!manager.scoreSelected() && !manager.isSardalme() && !manager.isKhoni()) {
                Toast.makeText(requireContext(), R.string.new_selection_error, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if (!manager.isSelectionOk()) {
            Toast.makeText(requireContext(), R.string.empty_error, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void reset() {
        selected = -1;
        CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMethod);
        adapter = new GridViewAdapterReasonAddReport(requireContext(), manager.getScoreTop());
        adapterTwo = new GridViewAdapterReasonAddReport(requireContext(), manager.getScoreBottom());
        gridView.setAdapter(adapter);
        gridViewTwo.setAdapter(adapterTwo);
        cartieSelection.setText(R.string.cartie_selection_text);
    }
}