package ir.coleo.varam.activities.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.MainActivity;
import ir.coleo.varam.activities.tabs.marked_activities.MarkedCowsFragment;
import ir.coleo.varam.activities.tabs.marked_activities.MarkedFarmFragment;
import ir.coleo.varam.adapters.TabAdapter;

/**
 * صفحه مدیریت قسمت نشان‌شده ها در برنامه
 */
public class MarkedFragment extends Fragment {

    private TabAdapter adapter;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_marked, container, false);
        view.findViewById(R.id.menu_button).setOnClickListener(v -> {
            ((MainActivity) requireActivity()).openMenu();
        });
        ViewPager viewPager = view.findViewById(R.id.marked_pager_id);
        tabLayout = view.findViewById(R.id.marked_tab_layout_id);
        viewPager.setOffscreenPageLimit(2);

        adapter = new TabAdapter(requireContext(), requireActivity().getSupportFragmentManager());
        adapter.addFragment(new MarkedCowsFragment(), getResources().getString(R.string.cows));
        adapter.addFragment(new MarkedFarmFragment(), getResources().getString(R.string.livestrocks));

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