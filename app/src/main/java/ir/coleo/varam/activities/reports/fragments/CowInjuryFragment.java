package ir.coleo.varam.activities.reports.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.dialog.SelectFingerDialog;

import java.util.Objects;


public class CowInjuryFragment extends Fragment {

    private int selected = -1;
    private Boolean rightSide = null;
    private int[] btnIdRight = {R.id.one_right, R.id.two_right, R.id.three_right, R.id.four_right, R.id.five_right, R.id.six_right};
    private int[] imageIdRight = {R.drawable.ic_one_right, R.drawable.ic_two_right, R.drawable.ic_three_right, R.drawable.ic_four_right, R.drawable.ic_five_right, R.drawable.ic_six_right};
    private int[] btnIdLeft = {R.id.one_left, R.id.two_left, R.id.three_left, R.id.four_left, R.id.five_left, R.id.six_left};
    private int[] imageIdLeft = {R.drawable.ic_one_left, R.drawable.ic_two_left, R.drawable.ic_three_left, R.drawable.ic_four_left, R.drawable.ic_five_left, R.drawable.ic_six_left};
    private ImageView mainImage;
    private ImageView rightImage;
    private ImageView leftImage;

    public CowInjuryFragment(int selected, Boolean rightSide) {
        this.selected = selected;
        if (selected == 0 || selected >= 7) {
            this.rightSide = null;
        } else {
            this.rightSide = rightSide;
        }
    }

    public CowInjuryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cow_injury, container, false);
        Constants.setImageFront(requireContext(), view.findViewById(R.id.next_icon));
        Constants.setImageBack(requireContext(), view.findViewById(R.id.back_icon));

        mainImage = view.findViewById(R.id.main_som);
        rightImage = view.findViewById(R.id.right_som);
        leftImage = view.findViewById(R.id.left_som);
        view.findViewById(R.id.zero).setOnClickListener(v -> {
            if (selected == -1) {
                mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_zero));
                selected = 0;
                rightSide = null;
                getFingerNumber();
            } else if (selected == 0 && rightSide == null) {
                mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_som_main));
                selected = -1;
                rightSide = null;
            } else {
                errorOnlyOne();
            }
        });
        for (int i = 1; i < 7; i++) {
            int id = btnIdRight[i - 1];
            int drawable = imageIdRight[i - 1];
            int finalI = i;
            view.findViewById(id).setOnClickListener(v -> {
                if (selected == -1) {
                    mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawable));
                    selected = finalI;
                    rightSide = Boolean.TRUE;
                    getFingerNumber();
                } else if (selected == finalI && rightSide != null && rightSide.equals(Boolean.TRUE)) {
                    mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_som_main));
                    selected = -1;
                    rightSide = null;
                } else {
                    errorOnlyOne();
                }
            });
        }
        for (int i = 1; i < 7; i++) {
            int id = btnIdLeft[i - 1];
            int drawable = imageIdLeft[i - 1];
            int finalI = i;
            view.findViewById(id).setOnClickListener(v -> {
                if (selected == -1) {
                    mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawable));
                    selected = finalI;
                    rightSide = Boolean.FALSE;
                    getFingerNumber();
                } else if (selected == finalI && rightSide != null && rightSide.equals(Boolean.FALSE)) {
                    mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_som_main));
                    selected = -1;
                    rightSide = null;
                } else {
                    errorOnlyOne();
                }
            });
        }
        view.findViewById(R.id.ten).setOnClickListener(v -> {
            if (selected == -1) {
                mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_ten));
                selected = 10;
                rightSide = null;
                getFingerNumber();
            } else if (selected == 10 && rightSide == null) {
                mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_som_main));
                selected = -1;
                rightSide = null;
            } else {
                errorOnlyOne();
            }
        });
        view.findViewById(R.id.eleven).setOnClickListener(v -> {
            if (selected == -1) {
                leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_eleven));
                selected = 11;
                getFingerNumber();
            } else if (selected == 11) {
                leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_20));
                selected = -1;
            } else {
                errorOnlyOne();
            }
        });
        view.findViewById(R.id.twelve).setOnClickListener(v -> {
            if (selected == -1) {
                leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_twelve));
                selected = 12;
                getFingerNumber();
            } else if (selected == 12) {
                leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_20));
                selected = -1;
            } else {
                errorOnlyOne();
            }
        });

        view.findViewById(R.id.eight).setOnClickListener(v -> {
            if (selected == -1) {
                rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_eight));
                selected = 8;
                getFingerNumber();
            } else if (selected == 8) {
                rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_16));
                selected = -1;
            } else {
                errorOnlyOne();
            }
        });
        view.findViewById(R.id.nine).setOnClickListener(v -> {
            if (selected == -1) {
                rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_nine));
                selected = 9;
                getFingerNumber();
            } else if (selected == 9) {
                rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_16));
                selected = -1;
            } else {
                errorOnlyOne();
            }
        });
        view.findViewById(R.id.seven).setOnClickListener(v -> {
            if (selected == -1) {
                rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_seven));
                selected = 7;
                getFingerNumber();
            } else if (selected == 7) {
                rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_16));
                selected = -1;
            } else {
                errorOnlyOne();
            }
        });

        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            ((AddReportActivity) requireActivity()).next();
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            ((AddReportActivity) requireActivity()).back();
        });
        return view;
    }

    public void getFingerNumber() {
        SelectFingerDialog dialog = new SelectFingerDialog(requireContext());
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setOnDismissListener(dialogInterface -> Constants.hideKeyboard(requireActivity()));
        dialog.show();
    }

    public int getSelected() {
        return selected;
    }

    public Boolean getRightSide() {
        return rightSide;
    }

    private void errorOnlyOne() {
        Toast.makeText(requireContext(), R.string.toast_select_only_one, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (selected != -1) {
            if (selected > -1 && selected < 7 || selected == 10) {
                if (selected == 10) {
                    mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_ten));
                    rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_16));
                    leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_20));
                } else if (selected == 0) {
                    mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_zero));
                    rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_16));
                    leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_20));
                } else {
                    if (rightSide)
                        mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), imageIdRight[selected - 1]));
                    else
                        mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), imageIdLeft[selected - 1]));
                    rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_16));
                    leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_20));
                }
            } else if (selected > 10) {
                if (selected == 11) {
                    leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_eleven));
                } else {
                    leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_twelve));
                }
                mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_som_main));
                rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_16));
            } else {
                if (selected == 7) {
                    rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_seven));
                } else if (selected == 8) {
                    rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_eight));
                } else {
                    rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_nine));
                }
                mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_som_main));
                leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_20));
            }
        } else {
            mainImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_som_main));
            rightImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_16));
            leftImage.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_20));
        }
    }
}