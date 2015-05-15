package de.medieninf.mobcomp.currencyconverter.util;

import java.math.BigDecimal;

/**
 * Created by bchristiani on 30.04.2015.
 */
public final class CurrencyConverterUtil {

    public static enum Type {EURO_TO_OTHER, OTHER_TO_EURO};

    public static String convertEuroCurrency(final BigDecimal amount, final Type type, final BigDecimal rate, final int scale) {
        BigDecimal resultBd = BigDecimal.ZERO;
        if(type == Type.EURO_TO_OTHER) {
            resultBd = amount.multiply(rate).setScale(scale,BigDecimal.ROUND_HALF_UP);
        } else if(type == Type.OTHER_TO_EURO) {
            resultBd = amount.divide(rate, scale, BigDecimal.ROUND_HALF_UP);
        }
        return resultBd.toString();
    }

    public static String convertEuroCurrency(final String amount, final Type type, final float rate, final int scale) {
        BigDecimal amountBd = new BigDecimal(amount);
        BigDecimal rateBd = BigDecimal.valueOf(rate);
        return convertEuroCurrency(amountBd,type,rateBd, scale);
    }

    public static String convertOtherCurrency(final BigDecimal amount, final BigDecimal startRate, final BigDecimal targetRate, final int scale) {
        final String amountInEuro = convertEuroCurrency(amount, Type.OTHER_TO_EURO, startRate, scale);
        BigDecimal amountInEuroBd = new BigDecimal(amountInEuro);
        return convertEuroCurrency(amountInEuroBd, Type.EURO_TO_OTHER, targetRate, scale);
    }

    public static String convertOtherCurrency(final String amount, final float startRate, final float targetRate, final int scale) {
        BigDecimal amountBd = new BigDecimal(amount);
        BigDecimal startRateBd = BigDecimal.valueOf(startRate);
        BigDecimal targetRateBd = BigDecimal.valueOf(targetRate);
        return convertOtherCurrency(amountBd,startRateBd,targetRateBd,scale);
    }

    public static boolean isNumeric(final String s) {
        String value = s;
        if(s.endsWith(".")) {
            value = s.concat("0");
        }
        if(s.startsWith(".")) {
            value = "0".concat(s);
        }
        return value.matches("[-+]?\\d*\\.?\\d+");
    }

    private CurrencyConverterUtil() {
        throw new IllegalAccessError("Private constructor should never be used.");
    }
}
