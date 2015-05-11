package de.medieninf.mobcomp.currencyconverter.util;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;

/**
 * Created by bchristiani on 11.05.2015.
 */
public final class CurrencyEntryUtil {

    public static String parseCurrencyFromString(final String str) {
        String result = "";

        String [] splittedStr = str.split(" ");
        if(splittedStr.length > 1) {
            result = splittedStr[0];
        }

        return result;
    }

    private CurrencyEntryUtil() {
        throw new IllegalAccessError("Private constructor should never be used.");
    }
}
