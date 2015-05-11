package de.medieninf.mobcomp.currencyconverter.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class CurrencyRates {

    private Map<String, CurrencyRateEntry> currencyRates;
    private Date timestamp;

    public CurrencyRates() {
        currencyRates = new HashMap<>();
    }

    public void addCurrencyRateEntry(CurrencyRateEntry entry){
        currencyRates.put(entry.getCurrency(), entry);
    }

    public CurrencyRateEntry getRateEntry(String currency) {
        return currencyRates.get(currency);
    }

    public List<CurrencyRateEntry> getCurrencyRateEntries() {
        List<CurrencyRateEntry> list = new ArrayList<>();
        for(CurrencyRateEntry entry: currencyRates.values()) {
            list.add(entry);
        }
        return list;
    }

    public List<CharSequence> getCurrencies() {
        List<CharSequence> list = new ArrayList<>();
        for(CurrencyRateEntry entry: currencyRates.values()) {
            list.add(entry.getCurrency());
        }
        return list;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
