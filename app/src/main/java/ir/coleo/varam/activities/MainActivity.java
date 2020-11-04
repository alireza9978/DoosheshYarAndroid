package ir.coleo.varam.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.menu.ContactActivity;
import ir.coleo.varam.activities.menu.ProfileActivity;
import ir.coleo.varam.adapters.TabAdapterHome;
import ir.coleo.varam.constants.Constants;

import static ir.coleo.varam.constants.Constants.CHOOSE_FILE_REQUEST_CODE;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_FACTOR;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_INJURY;
import static ir.coleo.varam.constants.Constants.FARM_SELECTION_REPORT_FACTOR;
import static ir.coleo.varam.constants.Constants.FARM_SELECTION_REPORT_INJURY;
import static ir.coleo.varam.constants.Constants.getDefualtlanguage;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TabAdapterHome adapter;
    private TabLayout tabLayout;
    private DrawerLayout drawerLayout;

    public static void applyFontToMenu(Menu m, Context mContext) {
        for (int i = 0; i < m.size(); i++) {
            applyFontToMenuItem(m.getItem(i), mContext);
        }
    }

    public static void applyFontToMenuItem(MenuItem mi, Context mContext) {
        if (mi.hasSubMenu())
            for (int i = 0; i < mi.getSubMenu().size(); i++) {
                applyFontToMenuItem(mi.getSubMenu().getItem(i), mContext);
            }
        Typeface font = ResourcesCompat.getFont(mContext, R.font.anjoman_medium);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());

        mNewTitle.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.pager_id);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setUserInputEnabled(false);
        NavigationView navigationView = findViewById(R.id.navigation);
        drawerLayout = findViewById(R.id.drawer_layout);
        tabLayout = findViewById(R.id.tab_layout_id);
        adapter = new TabAdapterHome(this, tabLayout, viewPager);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setCustomView(null);
            tab.setCustomView(adapter.getTabView(position));
        }).attach();

        applyFontToMenu(navigationView.getMenu(), this);
        navigationView.setNavigationItemSelectedListener(this);
        tabLayout.selectTab(tabLayout.getTabAt(4));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.DATE_SELECTION_SEARCH_COW:
            case Constants.FARM_SELECTION_SEARCH_COW:
            case Constants.DATE_SELECTION_SEARCH_FARM:
                adapter.getFragment(3).onActivityResult(requestCode, resultCode, data);
                break;

            case DATE_SELECTION_REPORT_INJURY:
            case FARM_SELECTION_REPORT_INJURY:
            case DATE_SELECTION_REPORT_FACTOR:
            case FARM_SELECTION_REPORT_FACTOR: {
                adapter.getFragment(1).onActivityResult(requestCode, resultCode, data);
                break;
            }
            default: {
                adapter.getFragment(1).onActivityResult(CHOOSE_FILE_REQUEST_CODE, resultCode, data);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tabLayout.getSelectedTabPosition() == 2) {
            tabLayout.selectTab(tabLayout.getTabAt(4));
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_us:
            case R.id.user_guid:
                return true;
            case R.id.username: {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.contact: {
                Intent intent = new Intent(this, ContactActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.lang: {
                if (getDefualtlanguage(this).equals("fa")) {
                    Constants.setLanguage(this, "en");
                } else {
                    Constants.setLanguage(this, "fa");
                }
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        }
        return false;
    }

    public void openMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static class CustomTypefaceSpan extends TypefaceSpan {

        private final Typeface newType;

        public CustomTypefaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        private static void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }

        @Override
        public void updateDrawState(@NotNull TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(@NotNull TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }
    }

}