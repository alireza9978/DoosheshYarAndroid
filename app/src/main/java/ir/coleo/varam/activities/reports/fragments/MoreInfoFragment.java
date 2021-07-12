package ir.coleo.varam.activities.reports.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.DateSelectionActivity;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.models.CheckBoxManager;

/**
 * صفحه ثبت اطلاعات بیشتر در ثبت گزارش
 */
public class MoreInfoFragment extends Fragment {

    private EditText moreInfo;
    private ConstraintLayout date_container;
    private TextView date_text;
    private String date;
    private String description;
    private boolean scoreMode;

    public MoreInfoFragment(String date, String description, boolean scoreMode) {
        this.date = date;
        this.description = description;
        this.scoreMode = scoreMode;
    }

    public MoreInfoFragment(boolean scoreMode) {
        this.scoreMode = scoreMode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_info, container, false);
        Constants.setImageBack(requireContext(), view.findViewById(R.id.back_icon));
        moreInfo = view.findViewById(R.id.more_info_edit);
        date_container = view.findViewById(R.id.date_container);
        date_text = view.findViewById(R.id.date_text);

        date_container.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.SINGLE);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_REPORT_CREATE_END);
        });

        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            if (!CheckBoxManager.getCheckBoxManager(scoreMode).isTarkhis()) {
                if (CheckBoxManager.getCheckBoxManager(scoreMode).isKor()) {
                    ((AddReportActivity) requireActivity()).next();
                } else if (date != null) {
                    ((AddReportActivity) requireActivity()).next();
                } else {
                    Toast.makeText(requireContext(), R.string.enter_next_visit_date, Toast.LENGTH_LONG).show();
                }
            } else {
                ((AddReportActivity) requireActivity()).next();
            }

        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            ((AddReportActivity) requireActivity()).back();
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        this.description = moreInfo.getText().toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (description != null) {
            moreInfo.setText(description);
        }
        if (date != null) {
            if (this.date.length() == 0) {
                date_container.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
            } else {
                date_container.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
            }
            date_text.setText(date);
        }
    }

    public String getMoreInfo() {
        this.description = moreInfo.getText().toString();
        return description;
    }

    public void setDate(String date) {
        if (date != null) {
            this.date = date;
            date_text.setText(date);
        }
    }


}