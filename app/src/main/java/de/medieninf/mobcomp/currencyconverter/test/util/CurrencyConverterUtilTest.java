package de.medieninf.mobcomp.currencyconverter.test.util;

import android.test.InstrumentationTestCase;

import de.medieninf.mobcomp.currencyconverter.util.CurrencyConverterUtil;

/**
 * Created by bchristiani on 30.04.2015.
 */
public class CurrencyConverterUtilTest extends InstrumentationTestCase{

    private static float GRD_RATE = 340.24f;
    private static float USD_RATE = 1.11f;

    public void testConvertEuroToOtherCurrency() throws Exception{
        final String input = "17.24";
        final String expected = "5865.74";
        final String result = CurrencyConverterUtil.convertEuroCurrency(input, CurrencyConverterUtil.Type.EURO_TO_OTHER, GRD_RATE);
        assertEquals(expected,result);
    }

    public void testConvertOtherToEuroCurrency() throws Exception{
        final String input = "1000";
        final String expected = "2.94";
        final String result = CurrencyConverterUtil.convertEuroCurrency(input, CurrencyConverterUtil.Type.OTHER_TO_EURO, GRD_RATE);
        assertEquals(expected,result);
    }

    public void testConvertOtherToOtherCurrency() throws Exception{
        final String input = "17.24";
        final String expected = "5283.93";
        final String result = CurrencyConverterUtil.convertOtherCurrency(input, USD_RATE, GRD_RATE);
        assertEquals(expected,result);
    }
}
