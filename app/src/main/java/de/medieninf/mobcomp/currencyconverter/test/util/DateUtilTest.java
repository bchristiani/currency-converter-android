package de.medieninf.mobcomp.currencyconverter.test.util;

import android.test.InstrumentationTestCase;

import java.util.Calendar;
import java.util.Date;

import de.medieninf.mobcomp.currencyconverter.util.DateUtil;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class DateUtilTest extends InstrumentationTestCase{

    public void testParseStringToDate() throws Exception{
        final String dateAsString = "2015-04-30";
        DateUtil.parseStringToDate(dateAsString, "yyy-MM-DD");
    }

    public void testParseStringToDate2() throws Exception {
        final String dateAsString = "Mon, 11 May 2015 12:00:01 GMT";
        DateUtil.parseStringToDate(dateAsString, "EEE, dd MMMMM yyyy hh:mm:ss z");
    }

    public void testIsToday() throws Exception {
        final Date today = Calendar.getInstance().getTime();
        assertTrue(DateUtil.isToday(today));
    }
}
