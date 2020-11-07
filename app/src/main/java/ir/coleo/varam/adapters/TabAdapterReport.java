package ir.coleo.varam.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ir.coleo.varam.activities.reports.fragments.CartieStateFragment;
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
    private int legAreaNumber;
    private String date;
    private String nextDate;
    private String description;
    private boolean rightSide;
    private final boolean edit;
    private boolean scoreMode;

    public TabAdapterReport(@NonNull FragmentActivity fragmentActivity, boolean scoreMode) {
        super(fragmentActivity);
        this.edit = false;
        this.scoreMode = scoreMode;
    }

    public TabAdapterReport(@NonNull FragmentActivity fragmentActivity, int cowNumber, String date,
                            String nextDate, int legAreaNumber, boolean rightSide, String description) {
        super(fragmentActivity);
        this.edit = true;
        this.description = description;
        this.cowNumber = cowNumber;
        this.date = date;
        this.nextDate = nextDate;
        this.legAreaNumber = legAreaNumber;
        this.rightSide = rightSide;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (fragments[position] == null)
            switch (position) {
                case 4: {
                    fragments[4] = new MoreInfoFragment();
                    break;
                }
                case 3: {
                    if (edit) {
//                        fragments[3] = new DrugFragment(nextDate, description);
                    } else {
                        fragments[3] = new DrugFragment();
                    }
                    break;
                }
                case 2: {
                    if (edit) {
//                        fragments[2] = new CartieStateFragment();
                    } else {
                        fragments[2] = new CartieStateFragment(scoreMode);
                    }
                    break;
                }
                case 1: {
                    fragments[1] = new CowInjuryFragment(scoreMode);
                    break;
                }
                case 0: {
                    if (edit) {
                        fragments[0] = new CowInfoFragment();
//                        ((CowInfoFragment) fragments[0]).setCowInfoFragment(cowNumber, date);
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
        return 5;
    }
}