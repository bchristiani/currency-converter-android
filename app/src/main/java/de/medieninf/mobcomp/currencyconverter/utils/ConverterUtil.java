package de.medieninf.mobcomp.currencyconverter.utils;

import java.math.BigDecimal;

/**
 * Created by bchristiani on 30.04.2015.
 */
public final class ConverterUtil {

    public static enum Type {EURO_TO_OTHER, OTHER_TO_EURO};

    public static float convertEuroCurrency(final float amount, final Type type, final float rate) {
        BigDecimal amountBd = BigDecimal.valueOf(amount);
        BigDecimal rateBd = BigDecimal.valueOf(rate);
        BigDecimal resultBd = BigDecimal.ZERO;
        if(type == Type.EURO_TO_OTHER) {
            resultBd = amountBd.multiply(rateBd);
        } else if(type == Type.OTHER_TO_EURO) {
            resultBd = amountBd.divide(rateBd, 2, BigDecimal.ROUND_HALF_UP);
        }
        return resultBd.floatValue();
    }

    public static float convertOtherCurrency(final float amount, final float startRate, final float targetRate) {
        final float amountInEuro = convertEuroCurrency(amount, Type.OTHER_TO_EURO, startRate);
        return convertEuroCurrency(amountInEuro, Type.EURO_TO_OTHER, targetRate);
    }

    private ConverterUtil() {
        throw new IllegalAccessError("Private constructor should never be used.");
    }
}
