package ir.coleo.varam.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.res.ResourcesCompat;

import com.dariushm2.PersianCaldroid.caldroidfragment.PersianCaldroidFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import calendar.PersianDate;
import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.constants.Utilities;
import ir.coleo.varam.models.DateContainer;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_NONE;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_RANGE;
import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_SINGLE;
import static ir.coleo.varam.constants.Constants.DateSelectionMode.RANG;
import static ir.coleo.varam.constants.Constants.DateSelectionMode.SINGLE;
import static ir.coleo.varam.models.DateContainer.MyDate;

/**
 * صفحه انتخاب تاریخ
 * دو حالت انتخاب تکی و بازه‌ای
 * دو نوع فارسی و انگیلیسی
 */
public class DateSelectionActivity extends AppCompatActivity {

    private Context context = this;
    private TextView month;
    private TextView year;
    private MaterialCalendarView calendar;
    private ImageView right;
    private ImageView left;
    private TextView startDate;
    private TextView endDate;
    private TextView clear;
    private Button submit;
    private boolean rang;
    private int state = 0;
    private PersianDate startPersianDate;
    private PersianDate endPersianDate;
    private PersianCaldroidFragment persianCaldroidFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);

        right = findViewById(R.id.right_arrow);
        ImageView close = findViewById(R.id.close_image);
        left = findViewById(R.id.left_arrow);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        clear = findViewById(R.id.clear_text);
        submit = findViewById(R.id.submit_date);
        month = findViewById(R.id.month_title);
        year = findViewById(R.id.year_title);
        calendar = findViewById(R.id.calendarView);
        FrameLayout calendarView = findViewById(R.id.persianCaldroid);

        String language = Constants.getDefaultLanguage(context);


        String action = getIntent().getAction();
        assert action != null;
        if (action.equals(RANG)) {
            calendar.setSelectionMode(SELECTION_MODE_RANGE);
            startDate.setVisibility(View.VISIBLE);
            endDate.setVisibility(View.VISIBLE);
            startDate.setText("");
            endDate.setText("");
            rang = true;
        } else if (action.equals(SINGLE)) {
            calendar.setSelectionMode(SELECTION_MODE_SINGLE);
            startDate.setVisibility(View.GONE);
            endDate.setVisibility(View.GONE);
            rang = false;
        } else {
            calendar.setSelectionMode(SELECTION_MODE_NONE);
            rang = false;
        }

        if (language.equals("en")) {
            calendar.setVisibility(View.VISIBLE);
            calendarView.setVisibility(View.INVISIBLE);
            setEnglish();
        } else {
            calendarView.setVisibility(View.VISIBLE);
            calendar.setVisibility(View.INVISIBLE);
            setPersian();
        }

        close.setOnClickListener(view -> finish());

    }

    @Override
    protected void onResume() {
        super.onResume();
        String language = Constants.getDefaultLanguage(context);

        if (language.equals("fa")) {
            View view = persianCaldroidFragment.getView();
            if (view != null) {
                AppCompatImageView next = view.findViewById(R.id.next);
                AppCompatImageView prev = view.findViewById(R.id.prev);
                next.setColorFilter(getResources().getColor(R.color.calender_blue));
                prev.setColorFilter(getResources().getColor(R.color.calender_blue));
            }
        }
    }

    private void setEnglish() {
        calendar.setTopbarVisible(false);
        calendar.setOnMonthChangedListener((widget, date) -> {
            Typeface font = ResourcesCompat.getFont(context, R.font.anjoman_bold);
            SpannableString mNewTitle = new SpannableString(Utilities.monthToString(date, context));

            mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            month.setText(mNewTitle);

            font = ResourcesCompat.getFont(context, R.font.anjoman_regular);
            mNewTitle = new SpannableString(Utilities.yearToString(date, context));

            mNewTitle.setSpan(new AbsoluteSizeSpan(16, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            year.setText(mNewTitle);
        });
        if (rang) {
            calendar.setOnRangeSelectedListener((widget, dates) -> {
                List<CalendarDay> days = calendar.getSelectedDates();
                CalendarDay startDay = days.get(0);
                CalendarDay endDay = days.get(days.size() - 1);
                DateContainer container = new DateContainer(RANG,
                        new MyDate(false, startDay.getDay(), startDay.getMonth(), startDay.getYear()),
                        new MyDate(false, endDay.getDay(), endDay.getMonth(), endDay.getYear()));
                startDate.setText(container.getStartDate().toStringWithoutYear(context));
                endDate.setText(container.getEndDate().toStringWithoutYear(context));
            });
            calendar.setOnDateChangedListener((widget, date, selected) -> {
                startDate.setText("");
                endDate.setText("");
            });
        }
        clear.setOnClickListener(view -> {
            calendar.clearSelection();
            CalendarDay today = CalendarDay.today();
            calendar.setCurrentDate(today);
            startDate.setText("");
            endDate.setText("");
        });
        right.setOnClickListener(v -> calendar.goToNext());
        left.setOnClickListener(v -> calendar.goToPrevious());
        submit.setOnClickListener((v) -> {
            Intent intent = new Intent();
            DateContainer container = null;
            if (calendar.getSelectionMode() == SELECTION_MODE_RANGE) {
                List<CalendarDay> days = calendar.getSelectedDates();
                CalendarDay startDay = days.get(0);
                CalendarDay endDay = days.get(days.size() - 1);
                container = new DateContainer(RANG,
                        new MyDate(false, startDay.getDay(), startDay.getMonth(), startDay.getYear()),
                        new MyDate(false, endDay.getDay(), endDay.getMonth(), endDay.getYear()));
            } else if (calendar.getSelectionMode() == SELECTION_MODE_SINGLE) {
                CalendarDay day = calendar.getSelectedDate();
                assert day != null;
                container = new DateContainer(SINGLE,
                        new MyDate(false, day.getDay(), day.getMonth(), day.getYear()));
            }
            if (container == null) {
                setResult(Constants.DATE_SELECTION_FAIL);
            }
            intent.putExtra(Constants.DATE_SELECTION_RESULT, container);
            setResult(Constants.DATE_SELECTION_OK, intent);
            finish();
        });

        Date date = new Date();
        CalendarDay today = CalendarDay.today();
        calendar.setSelectedDate(today);
        calendar.setCurrentDate(today);
        setTopCalendarBarEn(date);
    }

    private void setTopCalendarBarEn(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(date.getYear() + 1900, date.getMonth(), date.getDay());
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        SimpleDateFormat year_date = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String output_year = year_date.format(cal.getTime());
        String output_month = month_date.format(cal.getTime());
        Typeface font = ResourcesCompat.getFont(context, R.font.anjoman_bold);
        SpannableString mNewTitle = new SpannableString(output_year);

        mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.black)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        month.setText(mNewTitle);

        font = ResourcesCompat.getFont(context, R.font.anjoman_regular);
        mNewTitle = new SpannableString(output_month);

        mNewTitle.setSpan(new AbsoluteSizeSpan(16, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        year.setText(mNewTitle);
    }

    private void setPersian() {

        persianCaldroidFragment = new PersianCaldroidFragment();
        persianCaldroidFragment.setOnDateClickListener(persianDate -> {
            if (rang) {
                DateContainer dateContainer = new DateContainer(SINGLE,
                        new MyDate(true, persianDate.getDayOfMonth(), persianDate.getMonth(), persianDate.getYear()));
                if (state == 0) {
                    startDate.setText(dateContainer.toString(context));
                    startPersianDate = persianDate;
                    endPersianDate = null;
                    endDate.setText("");
                    state = 1;
                } else if (state == 1) {
                    if (startPersianDate.after(persianDate)) {
                        endPersianDate = startPersianDate;
                        startPersianDate = persianDate;
                        startDate.setText(dateContainer.toString(context));
                        dateContainer = new DateContainer(SINGLE,
                                new MyDate(true, endPersianDate.getDayOfMonth(), endPersianDate.getMonth(), endPersianDate.getYear()));
                    } else {
                        endPersianDate = persianDate;
                    }
                    endDate.setText(dateContainer.toString(context));
                    state = 0;
                }
            } else {
                startPersianDate = persianDate;
            }
        });
        persianCaldroidFragment.setOnChangeMonthListener(() -> {

            // Do something when user switches to previous or next month

        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.persianCaldroid,
                        persianCaldroidFragment,
                        PersianCaldroidFragment.class.getName()
                ).commit();

        submit.setOnClickListener((v) -> {
            Intent intent = new Intent();
            DateContainer container = null;
            if (rang) {
                if (startPersianDate == null || endPersianDate == null) {
                    Toast.makeText(context, R.string.range_date_error, Toast.LENGTH_LONG).show();
                    return;
                }
                container = new DateContainer(RANG,
                        new MyDate(true, startPersianDate.getDayOfMonth(), startPersianDate.getMonth(), startPersianDate.getYear()),
                        new MyDate(true, endPersianDate.getDayOfMonth(), endPersianDate.getMonth(), endPersianDate.getYear()));
            } else if (calendar.getSelectionMode() == SELECTION_MODE_SINGLE) {
                if (startPersianDate == null) {
                    Toast.makeText(context, R.string.single_date_error, Toast.LENGTH_LONG).show();
                    return;
                }
                container = new DateContainer(SINGLE,
                        new MyDate(true, startPersianDate.getDayOfMonth(), startPersianDate.getMonth(), startPersianDate.getYear()));
            }
            if (container == null) {
                setResult(Constants.DATE_SELECTION_FAIL);
            }
            intent.putExtra(Constants.DATE_SELECTION_RESULT, container);
            setResult(Constants.DATE_SELECTION_OK, intent);
            finish();
        });
        clear.setOnClickListener(v -> {
            if (rang) {
                state = 0;
                startPersianDate = null;
                startDate.setText("");
                endDate.setText("");
            } else {
                persianCaldroidFragment.selectDay(new PersianDate());
            }
        });

        findViewById(R.id.top_of_calendar).setVisibility(View.GONE);
    }

}