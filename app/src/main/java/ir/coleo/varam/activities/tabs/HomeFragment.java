package ir.coleo.varam.activities.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.MainActivity;
import ir.coleo.varam.activities.tabs.home_activites.VisitActivity;
import ir.coleo.varam.adapters.GridViewAdapterHomeFarm;
import ir.coleo.varam.adapters.RecyclerViewAdapterHomeNextVisit;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.models.FarmWithCowCount;
import ir.coleo.varam.database.models.NextReport;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.MyDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class HomeFragment extends Fragment {

    private ImageView downArrow;
    private TextView noFarmOne;
    private TextView noFarmTwo;
    private GridView farmsGrid;
    private GridViewAdapterHomeFarm adapterHomeFarm;

    private FancyButton showMore;
    private RecyclerView nextVisitList;
    private TextView noVisit;
    private RecyclerViewAdapterHomeNextVisit mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        farmsGrid = view.findViewById(R.id.livestocks_grid);
        downArrow = view.findViewById(R.id.down_arrow);
        noFarmOne = view.findViewById(R.id.create_first_livestock);
        noFarmTwo = view.findViewById(R.id.no_livestocks);

        showMore = view.findViewById(R.id.show_more);
        nextVisitList = view.findViewById(R.id.next_visit_lists);
        noVisit = view.findViewById(R.id.no_next_visit);

        view.findViewById(R.id.menu_button).setOnClickListener(v -> ((MainActivity) requireActivity()).openMenu());
        showMore.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), VisitActivity.class);
            startActivity(intent);
        });

        adapterHomeFarm = new GridViewAdapterHomeFarm(requireContext(), new ArrayList<>());
        farmsGrid.setAdapter(adapterHomeFarm);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        nextVisitList.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterHomeNextVisit(new ArrayList<>(), requireContext());
        nextVisitList.setAdapter(mAdapter);

        return view;
    }

    private void showVisit(boolean more) {
        requireActivity().runOnUiThread(() -> {
            nextVisitList.setVisibility(View.VISIBLE);
            if (more) {
                showMore.setVisibility(View.VISIBLE);
            } else {
                showMore.setVisibility(View.GONE);
            }
            noVisit.setVisibility(View.INVISIBLE);
        });
    }

    private void hideVisit() {
        requireActivity().runOnUiThread(() -> {
            nextVisitList.setVisibility(View.INVISIBLE);
            showMore.setVisibility(View.INVISIBLE);
            noVisit.setVisibility(View.VISIBLE);
        });
    }

    private void showFarms() {
        requireActivity().runOnUiThread(() -> {
            downArrow.setVisibility(View.INVISIBLE);
            noFarmOne.setVisibility(View.INVISIBLE);
            noFarmTwo.setVisibility(View.INVISIBLE);
            farmsGrid.setVisibility(View.VISIBLE);

        });
    }

    private void hideFarms() {
        requireActivity().runOnUiThread(() -> {
            downArrow.setVisibility(View.VISIBLE);
            noFarmOne.setVisibility(View.VISIBLE);
            noFarmTwo.setVisibility(View.VISIBLE);
            farmsGrid.setVisibility(View.INVISIBLE);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<FarmWithCowCount> farmList = dao.getFarmWithCowCount();
            List<Farm> farms = dao.getAll();
            if (farms.isEmpty()) {
                hideFarms();
            } else {
                requireActivity().runOnUiThread(() -> {
                    ArrayList<FarmWithCowCount> addition = new ArrayList<>();
                    main:
                    for (Farm farm : farms) {
                        for (FarmWithCowCount farmWithCowCount : farmList) {
                            if (farm.id.equals(farmWithCowCount.farmId)) {
                                continue main;
                            }
                        }
                        FarmWithCowCount temp = new FarmWithCowCount();
                        temp.cowCount = 0;
                        temp.farmId = farm.id;
                        temp.farmName = farm.name;
                        addition.add(temp);
                    }
                    farmList.addAll(addition);
                    adapterHomeFarm.setFarms(farmList);
                    adapterHomeFarm.notifyDataSetChanged();
                    showFarms();
                });
            }
        });
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<NextReport> reports = dao.getAllNextVisit(new MyDate(new Date()));
            Log.i("Visit", "onResume: " + reports.size());
            if (reports.isEmpty()) {
                hideVisit();
            } else {
                requireActivity().runOnUiThread(() -> {
                    showVisit(reports.size() > 3);
                    mAdapter.setNextReports(reports);
                    nextVisitList.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                });
            }
        });
    }
}