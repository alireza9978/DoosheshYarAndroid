package ir.coleo.varam.activities.tabs.search_activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.DateSelectionActivity;
import ir.coleo.varam.activities.FarmSelectionActivity;
import ir.coleo.varam.adapters.RecyclerViewAdapterSearchCow;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.CowForMarked;
import ir.coleo.varam.database.models.Farm;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.DateContainer;

import java.util.ArrayList;
import java.util.List;

public class SearchCowFragment extends Fragment {

    private RecyclerView cowsList;
    private TextView notFound;
    private TextView cowsListTitle;
    private TextView farmName;
    private EditText cowNumber;
    private ConstraintLayout cowNumberContainer;
    private ConstraintLayout farmContainer;
    private TextView dateText;
    private ConstraintLayout dateContainer;
    private RecyclerViewAdapterSearchCow mAdapter;

    private int farmId = -1;
    private DateContainer date = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_cow, container, false);

        cowsList = view.findViewById(R.id.searched_cows_list);
        Button search = view.findViewById(R.id.submit);
        cowsListTitle = view.findViewById(R.id.searched_cows_title);
        notFound = view.findViewById(R.id.not_fount_text);
        cowNumber = view.findViewById(R.id.cow_name_text);
        cowNumberContainer = view.findViewById(R.id.cow_number_container);
        farmContainer = view.findViewById(R.id.livestock_container);
        dateContainer = view.findViewById(R.id.date_container);
        farmName = view.findViewById(R.id.livestock_name_text);
        dateText = view.findViewById(R.id.date_text);

        cowsList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        cowsList.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterSearchCow(new ArrayList<>(), requireContext());
        cowsList.setAdapter(mAdapter);

        search.setOnClickListener((v) -> {
            MyDao dao = DataBase.getInstance(requireContext()).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                requireActivity().runOnUiThread(() -> Constants.hideKeyboard(requireActivity()));
                String cowIdString = cowNumber.getText().toString();
                if (cowIdString.isEmpty()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "input error", Toast.LENGTH_SHORT).show());
                    return;
                }
                cowIdString = "%" + cowIdString + "%";
                List<CowForMarked> cows;
                if (date != null && farmId != -1) {
                    Log.i("SEARCH", "onCreateView: " + cowIdString);
                    cows = dao.searchCow(cowIdString, date.exportStart(), date.exportEnd(), farmId);
                } else {
                    if (date == null) {
                        if (farmId == -1) {
                            Log.i("SEARCH", "onCreateView: single " + cowIdString);
                            cows = dao.searchCow(cowIdString);
                        } else {
                            cows = dao.searchCow(cowIdString, farmId);
                        }
                    } else {
                        cows = dao.searchCow(cowIdString, date.exportStart(), date.exportEnd());
                    }
                }
                requireActivity().runOnUiThread(() -> {
                    if (cows.isEmpty()) {
                        notFound();
                    } else {
                        found();
                        mAdapter.setCows(cows);
                        mAdapter.notifyDataSetChanged();
                    }

                });
            });

        });

        dateContainer.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.RANG);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_SEARCH_COW);
        });

        farmContainer.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), FarmSelectionActivity.class);
            requireActivity().startActivityForResult(intent, Constants.FARM_SELECTION_SEARCH_COW);
        });

        cowNumber.setOnFocusChangeListener((view13, b) -> {
            if (b) {
                cowNumberContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
            } else {
                if (cowNumber.getText().toString().isEmpty()) {
                    cowNumberContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                } else {
                    cowNumberContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notSearched();
    }

    public void setDate(DateContainer date) {
        this.date = date;
        dateText.setText(date.toStringSmall(requireContext()));

        if (dateText.getText().toString().isEmpty()) {
            dateContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
        } else {
            dateContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
        }

    }

    public void setFarm(int id) {
        this.farmId = id;
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Farm farm = dao.getFarm(id);
            if (farm != null)
                requireActivity().runOnUiThread(() -> {
                    farmName.setText(farm.name);
                    if (farmName.getText().toString().isEmpty()) {
                        farmContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                    } else {
                        farmContainer.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                    }
                });
        });
    }

    public void notSearched() {
        cowsList.setVisibility(View.INVISIBLE);
        notFound.setVisibility(View.INVISIBLE);
        cowsListTitle.setVisibility(View.INVISIBLE);
    }

    public void found() {
        cowsList.setVisibility(View.VISIBLE);
        notFound.setVisibility(View.INVISIBLE);
        cowsListTitle.setVisibility(View.VISIBLE);
    }

    public void notFound() {
        cowsList.setVisibility(View.INVISIBLE);
        notFound.setVisibility(View.VISIBLE);
        cowsListTitle.setVisibility(View.VISIBLE);
    }

}