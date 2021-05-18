package ir.coleo.varam.models;

import android.content.Context;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ir.coleo.varam.constants.FormatHelper;
import ir.coleo.varam.constants.Utilities;
import saman.zamani.persiandate.PersianDate;

import static ir.coleo.varam.constants.Constants.DateSelectionMode.RANG;
import static ir.coleo.varam.constants.Constants.DateSelectionMode.SINGLE;

/**
 * کلاس نگهدارنده‌ی اطلاعات تاریخ
 */
public class DateContainer implements Serializable {

    private String mode;
    private MyDate startDate;
    private MyDate endDate;

    public DateContainer(String mode, MyDate startDate) {
        this.mode = mode;
        this.startDate = startDate;
    }

    public DateContainer(String mode, MyDate startDate, MyDate endDate) {
        this.mode = mode;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static String toString(ir.coleo.varam.models.MyDate date) {
        if (date.compareTo(new ir.coleo.varam.models.MyDate(new Date())) == 0) {
            return "today";
        } else
            return "other_day";

    }

    public static DateContainer getToday(Context context, boolean persian) {
        Date date = new Date();
        ir.coleo.varam.models.MyDate myDate = new ir.coleo.varam.models.MyDate(date);
        int[] dateConverted = myDate.convert(context);
        return new DateContainer(SINGLE, new MyDate(persian, dateConverted[2], dateConverted[1], dateConverted[0]));
    }

    public int getRange() {
        if (startDate.persian) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.toGregorian(startDate.year, startDate.month, startDate.day);
            int[] tempEnd = pdate.toGregorian(endDate.year, endDate.month, endDate.day);

            Calendar startCal = new GregorianCalendar();
            Calendar endCal = new GregorianCalendar();

            startCal.set(temp[0], temp[1], temp[2]);
            endCal.set(tempEnd[0], tempEnd[1], tempEnd[2]);

            endCal.add(Calendar.YEAR, -startCal.get(Calendar.YEAR));
            endCal.add(Calendar.MONTH, -startCal.get(Calendar.MONTH));
            endCal.add(Calendar.DATE, -startCal.get(Calendar.DATE));

            return endCal.get(Calendar.DAY_OF_YEAR);

        } else {
            Calendar startCal = new GregorianCalendar();
            Calendar endCal = new GregorianCalendar();

            startCal.set(startDate.year, startDate.month, startDate.day);
            endCal.set(startDate.year, startDate.month, startDate.day);

            endCal.add(Calendar.YEAR, -startCal.get(Calendar.YEAR));
            endCal.add(Calendar.MONTH, -startCal.get(Calendar.MONTH));
            endCal.add(Calendar.DATE, -startCal.get(Calendar.DATE));
            return endCal.get(Calendar.DAY_OF_YEAR);
        }
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public MyDate getStartDate() {
        return startDate;
    }

    public void setStartDate(MyDate startDate) {
        this.startDate = startDate;
    }

    public MyDate getEndDate() {
        return endDate;
    }

    public void setEndDate(MyDate endDate) {
        this.endDate = endDate;
    }

    public String toStringSmall(Context context) {
        if (mode.equals(RANG)) {
            if (startDate.equals(endDate)) {
                return startDate.toStringWithoutYear(context);
            }
            return startDate.toStringWithoutYear(context) + "~" + endDate.toStringWithoutYear(context);
        } else {
            return startDate.toStringWithoutYear(context);
        }
    }

    public String toString(Context context) {
        if (mode.equals(RANG)) {
            if (startDate.equals(endDate)) {
                return startDate.toString(context);
            }
            return startDate.toString(context) + " " + endDate.toString(context);
        } else {
            return startDate.toString(context);
        }
    }

    public String toStringBeauty(Context context) {
        if (mode.equals(RANG)) {
            if (startDate.equals(endDate)) {
                return startDate.toString(context);
            }
            return "(" + startDate.toStringComa(context) + "~" + endDate.toStringComa(context) + ")";
        } else {
            return "(" + startDate.toStringComa(context) + ")";
        }
    }

    public ir.coleo.varam.models.MyDate exportStart() {
        if (startDate.persian) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.toGregorian(startDate.year, startDate.month, startDate.day);
            return new ir.coleo.varam.models.MyDate(temp[2], temp[1], temp[0]);
        } else {
            return new ir.coleo.varam.models.MyDate(startDate.day, startDate.month, startDate.year);
        }
    }

    public ir.coleo.varam.models.MyDate exportEnd() {
        if (endDate.persian) {
            PersianDate pdate = new PersianDate();
            int[] temp = pdate.toGregorian(endDate.year, endDate.month, endDate.day);
            return new ir.coleo.varam.models.MyDate(temp[2], temp[1], temp[0]);
        } else {
            return new ir.coleo.varam.models.MyDate(endDate.day, endDate.month, endDate.year);
        }
    }

    public static class MyDate implements Serializable {
        boolean persian;
        int day;
        int month;
        int year;

        public MyDate(boolean persian, int day, int month, int year) {
            this.persian = persian;
            this.day = day;
            this.month = month;
            this.year = year;
        }

        public boolean equals(MyDate obj) {
            return day == obj.day && month == obj.month && year == obj.year;
        }


        public String toString(Context context) {
            String out = "";
            if (persian) {
                out = FormatHelper.toPersianNumber("" + year);
            } else {
                out += year;
            }
            out = out + " " + Utilities.getMonthName(context, month);
            if (persian) {
                out = out + " " + FormatHelper.toPersianNumber("" + day);
            } else {
                out = out + " " + day;
            }
            return out;
        }

        public String toStringComa(Context context) {
            String out = "";
            if (persian) {
                out = FormatHelper.toPersianNumber("" + year);
            } else {
                out += year;
            }
            out = out + "," + Utilities.getMonthName(context, month);
            if (persian) {
                out = out + "," + FormatHelper.toPersianNumber("" + day);
            } else {
                out = out + "," + day;
            }
            return out;
        }

        public String toStringWithoutYear(Context context) {
            String out = Utilities.getMonthName(context, month);
            if (persian) {
                out = out + " " + FormatHelper.toPersianNumber("" + day);
            } else {
                out = out + " " + day;
            }
            return out;
        }

    }
}
