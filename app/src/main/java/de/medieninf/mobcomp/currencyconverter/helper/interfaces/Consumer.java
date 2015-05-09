package de.medieninf.mobcomp.currencyconverter.helper.interfaces;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;
import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;

/**
 * Created by bchristiani on 04.05.2015.
 */
public interface Consumer {
    public CurrencyRates parse(InputStream in) throws IOException;
}
