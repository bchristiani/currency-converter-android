package de.medieninf.mobcomp.currencyconverter.logic.interfaces;

import java.util.List;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;

/**
 * Created by bchristiani on 04.05.2015.
 */
public interface CurrencyRateProvider {
    public boolean updateRates(final String state);
    public float getRate(final String currency);
    public List<CharSequence> getCurrencies();
    public String getDate();
}
