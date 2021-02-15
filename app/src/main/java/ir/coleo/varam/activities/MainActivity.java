package ir.coleo.varam.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.menu.ContactActivity;
import ir.coleo.varam.activities.menu.DrugsListActivity;
import ir.coleo.varam.adapters.TabAdapterHome;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.service.MyNotificationPublisher;

import static ir.coleo.varam.constants.Constants.CHOOSE_FILE_REQUEST_CODE;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_FACTOR;
import static ir.coleo.varam.constants.Constants.DATE_SELECTION_REPORT_INJURY;
import static ir.coleo.varam.constants.Constants.FARM_SELECTION_REPORT_FACTOR;
import static ir.coleo.varam.constants.Constants.FARM_SELECTION_REPORT_INJURY;

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

    public void hideKeyboard() {
        Constants.hideKeyboard(this, findViewById(R.id.root).getWindowToken());
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
        tabLayout.selectTab(tabLayout.getTabAt(0));

        SwitchCompat switchCompat = navigationView.getMenu().getItem(1).getActionView().findViewById(R.id.switch_id);
        if (switchCompat != null) {
            switchCompat.setChecked(Constants.getNotificationStatus(this));
            switchCompat.setClickable(false);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.DATE_SELECTION_SEARCH_COW:
            case Constants.FARM_SELECTION_SEARCH_COW:
            case Constants.DATE_SELECTION_SEARCH_FARM:
                adapter.getFragment(1).onActivityResult(requestCode, resultCode, data);
                break;

            case DATE_SELECTION_REPORT_INJURY:
            case FARM_SELECTION_REPORT_INJURY:
            case DATE_SELECTION_REPORT_FACTOR:
            case FARM_SELECTION_REPORT_FACTOR: {
                adapter.getFragment(3).onActivityResult(requestCode, resultCode, data);
                break;
            }
            default: {
                adapter.getFragment(3).onActivityResult(CHOOSE_FILE_REQUEST_CODE, resultCode, data);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tabLayout.getSelectedTabPosition() == 2) {
            tabLayout.selectTab(tabLayout.getTabAt(0));
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_us: {
                Intent intent = new Intent(this, ContactActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.drugs: {
                Intent intent = new Intent(this, DrugsListActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.nav_switch: {
                SwitchCompat switchCompat = item.getActionView().findViewById(R.id.switch_id);
                if (switchCompat.isChecked()) {
                    switchCompat.setChecked(false);
                    Constants.setNotificationStatus(this, false);
                    cancelSchedule();
                } else {
                    switchCompat.setChecked(true);
                    Constants.setNotificationStatus(this, true);
                    scheduleNotification();
                }
                return true;
            }
//            case R.id.lang: {
//                if (getDefaultLanguage(this).equals("fa")) {
//                    Constants.setLanguage(this, "en");
//                } else {
//                    Constants.setLanguage(this, "fa");
//                }
//                Intent intent = new Intent(this, SplashActivity.class);
//                startActivity(intent);
//                finish();
//                return true;
//            }
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

    private void cancelSchedule() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

    }

    private void scheduleNotification() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent notificationIntent = new Intent(this, MyNotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}