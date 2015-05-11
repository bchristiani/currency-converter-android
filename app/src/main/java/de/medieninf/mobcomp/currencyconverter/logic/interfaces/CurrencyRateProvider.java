package de.medieninf.mobcomp.currencyconverter.logic.interfaces;

import java.util.List;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;
import de.medieninf.mobcomp.currencyconverter.persistence.interfaces.LoadManager;

/**
 * Created by bchristiani on 04.05.2015.
 */
public interface CurrencyRateProvider {
    public boolean updateRates(LoadManager.LoaderType type) throws Exception;
    public float getRate(final String currency);
    public List<CharSequence> getCurrencies();
    public String getDate(String format);
}
