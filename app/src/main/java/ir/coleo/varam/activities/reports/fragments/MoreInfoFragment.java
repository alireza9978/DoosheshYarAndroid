package ir.coleo.varam.activities.reports.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.DateSelectionActivity;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.adapters.GridViewAdapterReasonAddReport;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.models.CheckBoxManager;

public class MoreInfoFragment extends Fragment {

    private EditText moreInfo;
    private ConstraintLayout date_container;
    private TextView date_text;
    private String date;

    public MoreInfoFragment(String date) {
        this.date = date;
    }

    public MoreInfoFragment() {
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

        GridView gridView = view.findViewById(R.id.reason_container);
        GridViewAdapterReasonAddReport adapter = new GridViewAdapterReasonAddReport(requireContext(), CheckBoxManager.getCheckBoxManager().getMoreInfo());
        gridView.setAdapter(adapter);

        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            if (CheckBoxManager.getCheckBoxManager().moreInfoSelected()) {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                return;
            }
            ((AddReportActivity) requireActivity()).next();
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            ((AddReportActivity) requireActivity()).back();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        return moreInfo.getText().toString();
    }

    public void setDate(String date) {
        if (date != null) {
            this.date = date;
            date_text.setText(date);
        }
    }


}