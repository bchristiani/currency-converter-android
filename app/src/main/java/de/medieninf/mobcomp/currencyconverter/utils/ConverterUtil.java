package de.medieninf.mobcomp.currencyconverter.utils;

import java.math.BigDecimal;

/**
 * Created by bchristiani on 30.04.2015.
 */
public final class ConverterUtil {

    public static enum Type {EURO_TO_OTHER, OTHER_TO_EURO};

    public static String convertEuroCurrency(final BigDecimal amount, final Type type, final BigDecimal rate) {
        BigDecimal resultBd = BigDecimal.ZERO;
        if(type == Type.EURO_TO_OTHER) {
            resultBd = amount.multiply(rate).setScale(2,BigDecimal.ROUND_HALF_UP);
        } else if(type == Type.OTHER_TO_EURO) {
            resultBd = amount.divide(rate, 2, BigDecimal.ROUND_HALF_UP);
        }
        return resultBd.toString();
    }

    public static String convertEuroCurrency(final String amount, final Type type, final float rate) {
        BigDecimal amountBd = new BigDecimal(amount);
        BigDecimal rateBd = BigDecimal.valueOf(rate);
        return convertEuroCurrency(amountBd,type,rateBd);
    }

    public static String convertOtherCurrency(final BigDecimal amount, final BigDecimal startRate, final BigDecimal targetRate) {
        final String amountInEuro = convertEuroCurrency(amount, Type.OTHER_TO_EURO, startRate);
        BigDecimal amountInEuroBd = new BigDecimal(amountInEuro);
        return convertEuroCurrency(amountInEuroBd, Type.EURO_TO_OTHER, targetRate);
    }

    public static String convertOtherCurrency(final String amount, final float startRate, final float targetRate) {
        BigDecimal amountBd = new BigDecimal(amount);
        BigDecimal startRateBd = BigDecimal.valueOf(startRate);
        BigDecimal targetRateBd = BigDecimal.valueOf(targetRate);
        return convertOtherCurrency(amountBd,startRateBd,targetRateBd);
    }

    public static String getFormattedAmount(final float amount, final String currency) {
        return amount + " " + currency;
    }

    private ConverterUtil() {
        throw new IllegalAccessError("Private constructor should never be used.");
    }
}
