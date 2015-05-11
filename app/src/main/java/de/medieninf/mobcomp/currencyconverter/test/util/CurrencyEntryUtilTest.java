package de.medieninf.mobcomp.currencyconverter.test.util;

import android.test.InstrumentationTestCase;

import de.medieninf.mobcomp.currencyconverter.util.CurrencyEntryUtil;

/**
 * Created by bchristiani on 11.05.2015.
 */
public class CurrencyEntryUtilTest extends InstrumentationTestCase {

    public void test1ParseCurrencyFromString() throws Exception{
        final String input = "EUR (Euro)";
        final String expected = "EUR";
        final String actual = CurrencyEntryUtil.parseCurrencyFromString(input);
        assertEquals(expected, actual);
    }

    public void test2ParseCurrencyFromString() throws Exception{
        final String input = "XCD (East Caribbean Dollar)";
        final String expected = "XCD";
        final String actual = CurrencyEntryUtil.parseCurrencyFromString(input);
        assertEquals(expected, actual);
    }
}
