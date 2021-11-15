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
import ir.coleo.varam.database.models.main.ScoreMethod;


/**
 * مدیریت کننده اطلاعات در لیست
 * پایین صفحه اصلی
 */
public class TabAdapterReport extends FragmentStateAdapter {

    private final boolean edit;
    private final Fragment[] fragments = new Fragment[5];
    private int cowNumber;
    private int areaNumber;
    private String date;
    private String nextDate;
    private String description;
    private final ScoreMethod scoreMethod;
    private final int cowId;
    private ArrayList<Pair<Integer, Integer>> drugs;

    public TabAdapterReport(@NonNull FragmentActivity fragmentActivity, ScoreMethod scoreMethod, int cowId) {
        super(fragmentActivity);
        this.edit = false;
        this.scoreMethod = scoreMethod;
        this.cowId = cowId;
    }

    public TabAdapterReport(@NonNull FragmentActivity fragmentActivity, int cowNumber, String date,
                            String nextDate, int areaNumber, String description, ScoreMethod scoreMethod,
                            ArrayList<Pair<Integer, Integer>> drugs, int cowId) {
        super(fragmentActivity);
        this.edit = true;
        this.description = description;
        this.cowNumber = cowNumber;
        this.date = date;
        this.drugs = drugs;
        this.nextDate = nextDate;
        this.areaNumber = areaNumber;
        this.scoreMethod = scoreMethod;
        this.cowId = cowId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragments[position] == null)
            switch (position) {
                case 3: {
                    if (edit) {
                        fragments[3] = new MoreInfoFragment(nextDate, description, scoreMethod);
                    } else
                        fragments[3] = new MoreInfoFragment(scoreMethod);
                    break;
                }
                case 2: {
                    if (edit) {
                        fragments[2] = new DrugFragment(drugs, description, scoreMethod);
                    } else {
                        fragments[2] = new DrugFragment(scoreMethod);
                    }
                    break;
                }
                case 1: {
                    if (edit) {
                        fragments[1] = new CowInjuryFragment(areaNumber, scoreMethod, cowId);
                    } else {
                        fragments[1] = new CowInjuryFragment(scoreMethod, cowId);
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