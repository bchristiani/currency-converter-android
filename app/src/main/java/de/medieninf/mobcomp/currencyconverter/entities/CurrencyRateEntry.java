package de.medieninf.mobcomp.currencyconverter.entities;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class CurrencyRateEntry {

    private static final String CURRENCY_RATE_TEMPLATE = "%s (%s)";
    private String currency;
    private float rate;
    private String name;

    public CurrencyRateEntry() {

    }

    public CurrencyRateEntry(final String currency, final float rate) {
        this.currency = currency;
        this.rate  = rate;
    }

    public CurrencyRateEntry(final String currency, final String name, final float rate) {
        this(currency, rate);
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public float getRate() {
        return rate;
    }

    public String getName() { return name; }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        if(currency != null && name != null) {
            return String.format(CURRENCY_RATE_TEMPLATE, currency, name);
        }
        return "";
    }
}
