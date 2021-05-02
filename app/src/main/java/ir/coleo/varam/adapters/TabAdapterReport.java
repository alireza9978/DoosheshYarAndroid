package ir.coleo.varam.adapters;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import ir.coleo.varam.activities.reports.fragments.CowInfoFragment;
import ir.coleo.varam.activities.reports.fragments.CowInjuryFragment;
import ir.coleo.varam.activities.reports.fragments.DrugFragment;
import ir.coleo.varam.activities.reports.fragments.MoreInfoFragment;


/**
 * مدیریت کننده اطلاعات در لیست
 * پایین صفحه اصلی
 */
public class TabAdapterReport extends FragmentStateAdapter {

    private Fragment[] fragments = new Fragment[5];
    private int cowNumber;
    private int areaNumber;
    private String date;
    private String nextDate;
    private String description;
    private final boolean edit;
    private boolean scoreMode;
    private ArrayList<Pair<Integer, Integer>> drugs;

    public TabAdapterReport(@NonNull FragmentActivity fragmentActivity, boolean scoreMode) {
        super(fragmentActivity);
        this.edit = false;
        this.scoreMode = scoreMode;
    }

    public TabAdapterReport(@NonNull FragmentActivity fragmentActivity, int cowNumber, String date,
                            String nextDate, int areaNumber, String description, boolean scoreMode,
                            ArrayList<Pair<Integer, Integer>> drugs) {
        super(fragmentActivity);
        this.edit = true;
        this.description = description;
        this.cowNumber = cowNumber;
        this.date = date;
        this.drugs = drugs;
        this.nextDate = nextDate;
        this.areaNumber = areaNumber;
        this.scoreMode = scoreMode;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragments[position] == null)
            switch (position) {
                case 3: {
                    if (edit) {
                        fragments[3] = new MoreInfoFragment(nextDate, description, scoreMode);
                    } else
                        fragments[3] = new MoreInfoFragment(scoreMode);
                    break;
                }
                case 2: {
                    if (edit) {
                        fragments[2] = new DrugFragment(drugs);
                    } else {
                        fragments[2] = new DrugFragment();
                    }
                    break;
                }
                case 1: {
                    if (edit) {
                        fragments[1] = new CowInjuryFragment(areaNumber, scoreMode);
                    } else {
                        fragments[1] = new CowInjuryFragment(scoreMode);
                    }
                    break;
                }
                case 0: {
                    if (edit) {
                        fragments[0] = new CowInfoFragment(cowNumber, date);
                    } else {
                        fragments[0] = new CowInfoFragment();
                    }
                    break;
                }
            }
        return fragments[position];
    }

    public Fragment getFragment(int position) {
        if (fragments[position] == null) {
            return createFragment(position);
        }
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}