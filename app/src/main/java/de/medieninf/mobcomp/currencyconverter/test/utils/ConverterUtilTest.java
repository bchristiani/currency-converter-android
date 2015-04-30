package de.medieninf.mobcomp.currencyconverter.test.utils;

import android.test.InstrumentationTestCase;
import android.util.Log;

import de.medieninf.mobcomp.currencyconverter.utils.ConverterUtil;

/**
 * Created by bchristiani on 30.04.2015.
 */
public class ConverterUtilTest extends InstrumentationTestCase{

    private static String TAG = ConverterUtilTest.class.getSimpleName();
    private static float GRD_RATE = 340.24f;
    private static float USD_RATE = 1.11f;

    public void testConvertEuroToOtherCurrency() throws Exception{
        final String input = "17.24";
        final String expected = "5865.74";
        final String result = ConverterUtil.convertEuroCurrency(input, ConverterUtil.Type.EURO_TO_OTHER, GRD_RATE);
        assertEquals(expected,result);
    }

    public void testConvertOtherToEuroCurrency() throws Exception{
        final String input = "1000";
        final String expected = "2.94";
        final String result = ConverterUtil.convertEuroCurrency(input, ConverterUtil.Type.OTHER_TO_EURO, GRD_RATE);
        assertEquals(expected,result);
    }

    public void testConvertOtherToOtherCurrency() throws Exception{
        final String input = "17.24";
        final String expected = "5283.93";
        final String result = ConverterUtil.convertOtherCurrency(input, USD_RATE, GRD_RATE);
        assertEquals(expected,result);
    }
}
