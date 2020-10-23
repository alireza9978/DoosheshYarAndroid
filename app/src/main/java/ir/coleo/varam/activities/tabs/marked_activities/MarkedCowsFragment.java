package ir.coleo.varam.activities.tabs.marked_activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ir.coleo.varam.R;
import ir.coleo.varam.adapters.RecyclerViewAdapterSearchCow;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.CowForMarked;
import ir.coleo.varam.database.utils.AppExecutors;

import java.util.ArrayList;
import java.util.List;

public class MarkedCowsFragment extends Fragment {

    private RecyclerViewAdapterSearchCow mAdapter;
    private TextView notFound;
    private RecyclerView markedRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marked_cows, container, false);
        notFound = view.findViewById(R.id.no_marked_cow_text);
        markedRecyclerView = view.findViewById(R.id.marked_cow_list);

        markedRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        markedRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterSearchCow(new ArrayList<>(), requireContext());
        markedRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<CowForMarked> cows = dao.getMarkedCows();
            requireActivity().runOnUiThread(() -> {
                if (cows.isEmpty()) {
                    notFound.setVisibility(View.VISIBLE);
                    markedRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    notFound.setVisibility(View.INVISIBLE);
                    markedRecyclerView.setVisibility(View.VISIBLE);
                    mAdapter.setCows(cows);
                    mAdapter.notifyDataSetChanged();
                }

            });
        });
    }
}