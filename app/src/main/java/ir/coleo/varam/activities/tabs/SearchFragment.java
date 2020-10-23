package ir.coleo.varam.activities.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.MainActivity;
import ir.coleo.varam.activities.tabs.search_activities.SearchCowFragment;
import ir.coleo.varam.activities.tabs.search_activities.SearchFarmFragment;
import ir.coleo.varam.adapters.TabAdapter;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.models.DateContainer;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class SearchFragment extends Fragment {

    private TabAdapter adapter;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search, container, false);
        view.findViewById(R.id.menu_button).setOnClickListener(v -> {
            ((MainActivity) requireActivity()).openMenu();
        });
        ViewPager viewPager = view.findViewById(R.id.search_pager_id);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = view.findViewById(R.id.search_tab_layout_id);

        adapter = new TabAdapter(requireContext(), requireActivity().getSupportFragmentManager());
        adapter.addFragment(new SearchCowFragment(), getResources().getString(R.string.cows));
        adapter.addFragment(new SearchFarmFragment(), getResources().getString(R.string.livestrocks));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setupTabIcons();
        highLightCurrentTab(0);
        tabLayout.selectTab(tabLayout.getTabAt(0), true);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Constants.FARM_SELECTION_SEARCH_COW: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    int id = Objects.requireNonNull(data.getExtras()).getInt(Constants.FARM_ID);
                    ((SearchCowFragment) adapter.getItem(0)).setFarm(id);
                }
                break;
            }
            case Constants.DATE_SELECTION_SEARCH_COW: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(Constants.DATE_SELECTION_RESULT);
                    assert container != null;
                    ((SearchCowFragment) adapter.getItem(0)).setDate(container);
                }
            }
            case Constants.DATE_SELECTION_SEARCH_FARM: {
                if (resultCode == Constants.DATE_SELECTION_OK) {
                    assert data != null;
                    DateContainer container = (DateContainer) Objects.requireNonNull(data.getExtras()).get(Constants.DATE_SELECTION_RESULT);
                    assert container != null;
                    ((SearchFarmFragment) adapter.getItem(1)).setDate(container);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * مقدار دهی اولیه نوار پایین
     */
    private void setupTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    /**
     * تغییر رنگ صفحه فعال
     */
    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(adapter.getSelectedTabView(position));
    }

}