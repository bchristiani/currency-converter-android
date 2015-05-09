package de.medieninf.mobcomp.currencyconverter.entities;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class CurrencyRateEntry {

    private String currency;
    private float rate;

    public CurrencyRateEntry(final String currency, final float rate) {
        this.currency = currency;
        this.rate  = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public float getRate() {
        return rate;
    }
}
