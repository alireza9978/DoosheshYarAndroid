package ir.coleo.varam.activities.reports.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.DateSelectionActivity;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.constants.Constants;

public class CowInfoFragment extends Fragment {

    private ConstraintLayout date_container;
    private TextView date_text;
    private TextView numberEdit;
    private Integer cowNumber = null;
    private String date;

    public CowInfoFragment(Integer cowNumber, String date) {
        this.cowNumber = cowNumber;
        this.date = date;
    }

    public CowInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cow_info, container, false);
        Constants.setImageFront(requireContext(), view.findViewById(R.id.next_icon));

        date_container = view.findViewById(R.id.date_container);
        ConstraintLayout number_container = view.findViewById(R.id.cow_number_container);
        numberEdit = view.findViewById(R.id.cow_name_text);
        date_text = view.findViewById(R.id.date_text);

        numberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    number_container.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                } else {
                    number_container.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                }
            }
        });
        date_container.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.SINGLE);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_REPORT_CREATE);
        });

        ConstraintLayout button = view.findViewById(R.id.next_button);
        button.setOnClickListener(view1 -> {
            if (numberEdit.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), R.string.toast_enter_cow_number, Toast.LENGTH_SHORT).show();
                return;
            }
            if (date_text.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), R.string.toast_enter_date, Toast.LENGTH_SHORT).show();
                return;
            }
            Constants.hideKeyboard(requireActivity());
            ((AddReportActivity) requireActivity()).next();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cowNumber != null) {
            setCowNumber(cowNumber);
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


    public void setCowNumber(int number) {
        if (numberEdit != null) {
            numberEdit.setText("" + number);
            numberEdit.setEnabled(false);
            cowNumber = null;
        } else {
            cowNumber = number;
        }

    }

    public void setDate(String date) {
        if (date != null) {
            this.date = date;
            if (date_text != null)
                date_text.setText(date);
        }
    }

    public int getNumber() {
        return Integer.parseInt(numberEdit.getText().toString());
    }

}