package ir.coleo.varam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.login_fragments.LoginFragment;
import ir.coleo.varam.activities.login_fragments.SignUpFragment;
import ir.coleo.varam.adapters.TabAdapter;
import com.google.android.material.tabs.TabLayout;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

public class LoginActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView logo = findViewById(R.id.login_logo);
        ViewPager viewPager = findViewById(R.id.pager_id);
        tabLayout = findViewById(R.id.tab_layout_id);

        adapter = new TabAdapter(this, getSupportFragmentManager());
        adapter.addFragment(new SignUpFragment(), getResources().getString(R.string.sign_in));
        adapter.addFragment(new LoginFragment(), getResources().getString(R.string.login));

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


        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    if (isOpen) {
                        logo.setVisibility(View.GONE);
                    } else {
                        logo.setVisibility(View.VISIBLE);
                    }
                });

    }

    @Override
    public void onBackPressed() {

    }

    /**
     * بازکردن صفحه اصلی
     */
    public void goApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
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