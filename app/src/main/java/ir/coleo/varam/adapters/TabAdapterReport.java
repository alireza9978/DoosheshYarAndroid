package ir.coleo.varam.adapters;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * مدیریت کننده اطلاعات در لیست
 * پایین صفحه اصلی
 */
public class TabAdapterReport extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public TabAdapterReport(Context context, FragmentManager fm) {
        super(fm);
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


}