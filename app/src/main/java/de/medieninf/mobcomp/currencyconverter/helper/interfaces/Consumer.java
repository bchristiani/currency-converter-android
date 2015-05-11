package de.medieninf.mobcomp.currencyconverter.helper.interfaces;

import java.io.InputStream;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.exceptions.IllegalCurrencyException;

/**
 * Created by bchristiani on 04.05.2015.
 */
public interface Consumer {
    public CurrencyRates parse(InputStream in) throws IllegalCurrencyException;
}
