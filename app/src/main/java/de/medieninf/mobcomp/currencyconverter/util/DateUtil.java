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

    public static Date parseStringToDate(final String date, final String pattern) throws ParseException {
        DateFormat format = new SimpleDateFormat(pattern, FORMAT_LOCALE);
        return format.parse(date);
    }

    public static String formatDate(final Date date, final String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    private DateUtil() {
        throw new IllegalAccessError("Private constructor should never be used.");
    }
}
