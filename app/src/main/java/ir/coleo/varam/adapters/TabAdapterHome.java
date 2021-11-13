package ir.coleo.varam.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.tabs.AddFarmActivity;
import ir.coleo.varam.activities.tabs.BlankFragment;
import ir.coleo.varam.activities.tabs.HomeFragment;
import ir.coleo.varam.activities.tabs.MarkedFragment;
import ir.coleo.varam.activities.tabs.ReportsFragment;
import ir.coleo.varam.activities.tabs.SearchFragment;
import ir.coleo.varam.constants.Constants;


/**
 * مدیریت کننده اطلاعات در لیست
 * پایین صفحه اصلی
 */
public class TabAdapterHome extends FragmentStateAdapter {

    private TabLayout tabLayout;
    private LayoutInflater inflater;
    private Context context;
    private Fragment[] fragments = new Fragment[5];

    public TabAdapterHome(@NonNull FragmentActivity fragmentActivity, TabLayout tabLayout,
                          ViewPager2 pager) {
        super(fragmentActivity);
        this.context = fragmentActivity;
        this.tabLayout = tabLayout;
        inflater = LayoutInflater.from(context);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setCustomView(null);
                tab.setCustomView(getSelectedTabView(tab.getPosition()));
                if (tab.getPosition() == 2) {
                    Intent intent = new Intent(context, AddFarmActivity.class);
                    intent.putExtra(Constants.ADD_FARM_MODE, Constants.FARM_CREATE);
                    context.startActivity(intent);
                } else {
                    pager.setCurrentItem(tab.getPosition(), true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
                tab.setCustomView(getTabView(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public View getTabView(int position) {
        if (position == 2) {
            View view = inflater.inflate(R.layout.home_tab_layout_center, null);
            view.findViewById(R.id.item_image).setTag("add_farm_get");
            return view;
        }
        View view = inflater.inflate(R.layout.home_tab_layout, null);

        String title = null;
        Drawable drawable = null;
        switch (position) {
            case 4: {
                title = context.getResources().getString(R.string.marked);
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_bookmark);
                break;
            }
            case 3: {
                title = context.getResources().getString(R.string.report);
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_report);
                break;
            }
            case 1: {
                title = context.getResources().getString(R.string.search);
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_search);
                break;
            }
            case 0: {
                title = context.getResources().getString(R.string.home);
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_home);
            }
        }

        view.setTag(title);
        ImageView image = view.findViewById(R.id.item_image);
        image.setImageDrawable(drawable);
        image.setColorFilter(R.color.tab_home);
        return view;
    }

    public View getSelectedTabView(int position) {
        if (position == 2) {
            View view = inflater.inflate(R.layout.home_tab_layout_center, null);
            view.findViewById(R.id.item_image).setTag("add_farm_get");
            return view;
        }
        View view = inflater.inflate(R.layout.home_tab_layout_selected, null);


        String title = null;
        Drawable drawable = null;
        switch (position) {
            case 4: {
                title = context.getResources().getString(R.string.marked);
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_bookmark_fill);
                break;
            }
            case 3: {
                title = context.getResources().getString(R.string.report);
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_report_fill);
                break;
            }
            case 1: {
                title = context.getResources().getString(R.string.search);
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_search_fill);
                break;
            }
            case 0: {
                title = context.getResources().getString(R.string.home);
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_home_fill);
            }
        }

        TextView name = view.findViewById(R.id.item_name);
        name.setText(title);
        ImageView image = view.findViewById(R.id.item_image);
        image.setImageDrawable(drawable);
        image.setColorFilter(R.color.selected_tab_home);
        return view;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragments[position] == null)
            switch (position) {
                case 4: {
                    fragments[4] = new MarkedFragment();
                    break;
                }
                case 3: {
                    fragments[3] = new ReportsFragment();
                    break;
                }
                case 2: {
                    fragments[2] = new BlankFragment();
                    break;
                }
                case 1: {
                    fragments[1] = new SearchFragment();
                    break;
                }
                case 0: {
                    fragments[0] = new HomeFragment();
                    break;
                }
            }
        return fragments[position];
    }

    public Fragment getFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}