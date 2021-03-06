package ir.coleo.varam.activities.reports.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.DrugSelectionActivity;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Drug;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.models.main.ScoreMethod;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.CheckBoxManager;

/**
 * صفحه ثبت دارو‌های مصرفی در ثبت گزارش
 */
public class DrugFragment extends Fragment {

    ArrayList<Pair<Integer, Integer>> setDrugs;
    ArrayList<TextView> drugTextList = new ArrayList<>();
    private final int[] drugsId = new int[]{R.id.drug_text_one, R.id.drug_text_two,
            R.id.drug_text_three, R.id.drug_text_four, R.id.drug_text_five};
    private String description;
    private EditText moreInfo;
    private final ScoreMethod scoreMethod;


    public DrugFragment(ArrayList<Pair<Integer, Integer>> setDrugs, String description, ScoreMethod scoreMethod) {
        this.setDrugs = setDrugs;
        this.description = description;
        this.scoreMethod =scoreMethod;
    }

    public DrugFragment(ScoreMethod scoreMethod) {
        this.scoreMethod =scoreMethod;
        setDrugs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drug_add_report, container, false);
        Constants.setImageBack(requireContext(), view.findViewById(R.id.back_icon));
        moreInfo = view.findViewById(R.id.more_info_edit);

        for (int i = 0; i < drugsId.length; i++) {
            int finalI = i;
            drugTextList.add(view.findViewById(drugsId[i]));
            drugTextList.get(i).setOnClickListener(view1 -> {
                Intent intent = new Intent(requireContext(), DrugSelectionActivity.class);
                intent.putExtra(Constants.DRUG_TYPE, finalI);
                requireActivity().startActivityForResult(intent, Constants.DRUG_SELECTION);
            });
        }

        for (Pair<Integer, Integer> pair : setDrugs) {
            updateText(pair);
        }

        view.findViewById(R.id.next_button).setOnClickListener(v -> {
            this.description = moreInfo.getText().toString();
            CheckBoxManager manager = CheckBoxManager.getCheckBoxManager(this.scoreMethod);
            if(manager.isNew() || manager.isCureChange()){
                if (this.description.isEmpty() && !isDrugEntered()){
                    Toast.makeText(requireActivity(), getString(R.string.enter_drug_or_description), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            ((AddReportActivity) requireActivity()).next();
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> ((AddReportActivity) requireActivity()).back());
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
    }

    private void updateText(Pair<Integer, Integer> pair) {
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Drug temp = dao.getDrug(pair.second);
            requireActivity().runOnUiThread(() -> {
                drugTextList.get(pair.first).setText(temp.name);
                drugTextList.get(pair.first).setTextColor(requireContext().getResources().getColor(R.color.black));
            });
        });
    }

    public void setDrug(int type, int id) {
        requireActivity().runOnUiThread(() -> {
            Pair<Integer, Integer> pair = new Pair<>(type, id);
            int find = -1;
            for (int i = 0; i < setDrugs.size(); i++) {
                Pair<Integer, Integer> temp = setDrugs.get(i);
                if (Objects.equals(temp.first, pair.first)) {
                    find = i;
                    break;
                }
            }
            if (find != -1) {
                setDrugs.remove(find);
                setDrugs.add(pair);
            }
            setDrugs.add(pair);
            updateText(pair);
        });
    }

    public boolean isDrugEntered(){
        return setDrugs.size() > 0;
    }

    public void setDrugOnReport(Report report) {
        for (Pair<Integer, Integer> pair : setDrugs) {
            switch (pair.first) {
                case 0: {
                    report.pomadeId = pair.second;
                    break;
                }
                case 1: {
                    report.antibioticId = pair.second;
                    break;
                }
                case 2: {
                    report.serumId = pair.second;
                    break;
                }
                case 3: {
                    report.cureId = pair.second;
                    break;
                }
                case 4: {
                    report.antiInflammatoryId = pair.second;
                    break;
                }
            }
        }
    }

    public String getMoreInfo() {
        return description;
    }

}