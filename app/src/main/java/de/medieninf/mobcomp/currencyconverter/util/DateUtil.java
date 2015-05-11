package de.medieninf.mobcomp.currencyconverter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bchristiani on 04.05.2015.
 */
public final class DateUtil {

    private static Locale FORMAT_LOCALE = Locale.ENGLISH;

    public static boolean isToday(Date date) {
        Calendar calToday = Calendar.getInstance();
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);

        return (calToday.get(Calendar.ERA) == calDate.get(Calendar.ERA) &&
                calToday.get(Calendar.YEAR) == calDate.get(Calendar.YEAR) &&
                calToday.get(Calendar.DAY_OF_YEAR) == calDate.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static Date parseStringToDate(final String date, final String pattern) throws ParseException {
        DateFormat format = new SimpleDateFormat(pattern, FORMAT_LOCALE);
        return format.parse(date);
    }

    public static String formatDate(final Date date, final String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public static boolean isWeekend() {
        Calendar today = Calendar.getInstance();
        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return true;
        } else {
            return false;
        }
    }

    public static Date getLastFriday() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return today.getTime();
    }

    private DateUtil() {
        throw new IllegalAccessError("Private constructor should never be used.");
    }
}
