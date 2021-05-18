package ir.coleo.varam.activities.tabs.report_activites;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.MainActivity;
import ir.coleo.varam.adapters.RecyclerViewAdapterNextVisitReport;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.constants.FormatHelper;
import ir.coleo.varam.constants.Utilities;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.NextReport;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.DateContainer;
import ir.coleo.varam.models.MyDate;
import saman.zamani.persiandate.PersianDate;

import static ir.coleo.varam.constants.Constants.DateSelectionMode.SINGLE;

/**
 * صفحه بررسی بازدید‌های بعدی و قبلی در گزارش‌ها
 */
public class ReportVisitFragment extends Fragment {

    private TextView month;
    private TextView year;
    private Context context;
    private MaterialCalendarView calendar;
    private ImageView right;
    private ImageView left;

    private RecyclerViewAdapterNextVisitReport mAdapter;
    private TextView dateText;
    private TextView visitText;
    private TextView noVisit;
    private RecyclerView nextVisitList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_visit, container, false);
        context = requireContext();
        right = view.findViewById(R.id.right_arrow);
        left = view.findViewById(R.id.left_arrow);
        month = view.findViewById(R.id.month_title);
        year = view.findViewById(R.id.year_title);
        calendar = view.findViewById(R.id.calendarView);
        dateText = view.findViewById(R.id.date_text);
        dateText.setTextColor(getResources().getColor(R.color.hit_gray));
        noVisit = view.findViewById(R.id.not_fount_text);
        visitText = view.findViewById(R.id.visitDate);
        nextVisitList = view.findViewById(R.id.next_visits_list);

        if (Constants.getDefaultLanguage(context).equals("fa")) {
            setPersian();
        } else {
            setEnglish();
        }


        nextVisitList.setVisibility(View.INVISIBLE);
        noVisit.setVisibility(View.VISIBLE);

        nextVisitList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        nextVisitList.setLayoutManager(layoutManager);
        mAdapter = new RecyclerViewAdapterNextVisitReport(new ArrayList<>(), context);
        nextVisitList.setAdapter(mAdapter);
        return view;
    }


    private void setNotFound() {
        dateText.setVisibility(View.INVISIBLE);
        nextVisitList.setVisibility(View.INVISIBLE);
        noVisit.setVisibility(View.VISIBLE);
    }

    private void setFound() {
        dateText.setVisibility(View.VISIBLE);
        nextVisitList.setVisibility(View.VISIBLE);
        noVisit.setVisibility(View.INVISIBLE);
    }

    private void hideAll() {
        dateText.setVisibility(View.INVISIBLE);
        nextVisitList.setVisibility(View.INVISIBLE);
        noVisit.setVisibility(View.INVISIBLE);
        visitText.setVisibility(View.INVISIBLE);
    }

    private void setEnglish() {
        calendar.setTopbarVisible(false);
        calendar.setWeekDayFormatter(dayOfWeek -> {
            Typeface font = ResourcesCompat.getFont(context, R.font.anjoman_bold);
            SpannableString mNewTitle = new SpannableString(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));

            mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.hit_gray)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return mNewTitle;
        });
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
        right.setOnClickListener(v -> calendar.goToNext());
        left.setOnClickListener(v -> calendar.goToPrevious());
        calendar.setOnDateChangedListener((widget, date, selected) -> {

            MyDao dao = DataBase.getInstance(context).dao();
            DateContainer container = new DateContainer(SINGLE,
                    new DateContainer.MyDate(false, date.getDay(), date.getMonth(), date.getYear()));
            AppExecutors.getInstance().diskIO().execute(() -> {
                List<NextReport> list;
                if (selected) {
                    if (!date.isBefore(CalendarDay.today())) {
                        list = dao.getAllNextVisitInDay(container.exportStart());
                        ((Activity) context).runOnUiThread(() -> visitText.setText(R.string.next_visits));
                    } else {
                        list = dao.getAllVisitInDay(container.exportStart());
                        ((Activity) context).runOnUiThread(() -> visitText.setText(R.string.visits));
                    }
                    ((Activity) context).runOnUiThread(() -> {
                        dateText.setText(container.toStringBeauty(context));
                        dateText.setVisibility(View.VISIBLE);
                        Log.i("TAG", "setEnglish: " + "here");
                        mAdapter.setNextReports(list);
                        mAdapter.notifyDataSetChanged();
                        if (list.isEmpty()) {
                            setNotFound();
                        } else {
                            setFound();
                        }
                    });
                } else {
                    ((Activity) context).runOnUiThread(() -> {
                        dateText.setText("");
                        hideAll();
                    });
                }
            });

        });


        CalendarDay today = CalendarDay.today();
        calendar.setSelectedDate(today);
        calendar.setCurrentDate(today);
        setTopBarNew(today);
    }

    private void setTopBarNew(CalendarDay date) {
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
    }

    private void setPersian() {
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
        right.setOnClickListener(v -> calendar.goToNext());
        left.setOnClickListener(v -> calendar.goToPrevious());
        calendar.setDayFormatter(day -> {
            MyDate myDate = new MyDate(day.getDay(), day.getMonth(), day.getYear());
            return FormatHelper.toPersianNumber(myDate.convertDay(requireContext()));
        });
        calendar.setWeekDayFormatter(dayOfWeek -> {
            String weekDayString = " ";
            switch (dayOfWeek) {
                case MONDAY:
                    weekDayString = "د";
                    break;
                case TUESDAY:
                    weekDayString = "س";
                    break;
                case WEDNESDAY:
                    weekDayString = "چ";
                    break;
                case THURSDAY:
                    weekDayString = "پ";
                    break;
                case FRIDAY:
                    weekDayString = "ج";
                    break;
                case SATURDAY:
                    weekDayString = "ش";
                    break;
                case SUNDAY:
                    weekDayString = "ی";
                    break;
            }


            Typeface font = ResourcesCompat.getFont(context, R.font.anjoman_medium);
            SpannableString mNewTitle = new SpannableString(weekDayString);

            mNewTitle.setSpan(new AbsoluteSizeSpan(20, true), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.persian_green)), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mNewTitle.setSpan(new MainActivity.CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return mNewTitle;
        });
        calendar.setOnDateChangedListener((widget, date, selected) -> {
            MyDao dao = DataBase.getInstance(context).dao();
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.toJalali(date.getYear(), date.getMonth(), date.getDay());
            DateContainer container = new DateContainer(SINGLE,
                    new DateContainer.MyDate(true, temp[2], temp[1], temp[0]));
            AppExecutors.getInstance().diskIO().execute(() -> {
                List<NextReport> list;
                if (selected) {
                    if (!date.isBefore(CalendarDay.today())) {
                        list = dao.getAllNextVisitInDay(container.exportStart());
                        ((Activity) context).runOnUiThread(() -> visitText.setText(R.string.next_visits));
                    } else {
                        list = dao.getAllVisitInDay(container.exportStart());
                        ((Activity) context).runOnUiThread(() -> visitText.setText(R.string.visits));
                    }
                    ((Activity) context).runOnUiThread(() -> {
                        dateText.setText(container.toStringBeauty(context));
                        mAdapter.setNextReports(list);
                        mAdapter.notifyDataSetChanged();
                        if (list.isEmpty()) {
                            setNotFound();
                        } else {
                            setFound();
                        }
                    });
                } else {
                    ((Activity) context).runOnUiThread(() -> {
                        dateText.setText("");
                        hideAll();
                    });
                }
            });
        });


        CalendarDay today = CalendarDay.today();
        calendar.setSelectedDate(today);
        calendar.setCurrentDate(today);
        setTopBarNew(today);
    }
}