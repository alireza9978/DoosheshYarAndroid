package ir.coleo.varam.activities.reports.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.dialog.VaramInfoDialog;
import ir.coleo.varam.models.CheckBoxManager;

/**
 * صفحه صبت جراحت پستان در ثبت گزارش
 */
public class CowInjuryFragment extends Fragment {

    private int selected = -1;
    private ImageView mainImage;
    private int[] buttonId = new int[]{R.id.one, R.id.two, R.id.three, R.id.four};
    private int[] cartieImage = new int[]{R.drawable.ic_cartie_one, R.drawable.ic_cartie_two,
            R.drawable.ic_cartie_three, R.drawable.ic_cartie_four};
    private final ScoreMethod scoreMethod;
    private boolean edit = false;

    public CowInjuryFragment(int selected, ScoreMethod scoreMethod) {
        this.edit = true;
        this.selected = selected;
        this.scoreMethod = scoreMethod;
    }

    public CowInjuryFragment(ScoreMethod scoreMethod) {
        this.scoreMethod = scoreMethod;
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
            ((AddReportActivity) requireActivity()).next();
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            ((AddReportActivity) requireActivity()).back();
        });
        return view;
    }

    public void getFingerNumber() {
        VaramInfoDialog dialog = new VaramInfoDialog(this, edit, scoreMethod);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setOnDismissListener(dialogInterface -> {
            CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(scoreMethod);
            if (!manager.isTarkhis() && !manager.isKor()) {
                if (!manager.scoreSelected() || !manager.cartieSelected()) {
                    reset();
                }
            }
            ((AddReportActivity) requireActivity()).hideKeyboard();
        });
        dialog.show();
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
}