package de.medieninf.mobcomp.currencyconverter.logic;

import android.database.sqlite.SQLiteDatabase;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;
import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.logic.interfaces.CurrencyRateProvider;
import de.medieninf.mobcomp.currencyconverter.persistence.DatabaseLoadManager;
import de.medieninf.mobcomp.currencyconverter.persistence.DatabaseStoreManager;
import de.medieninf.mobcomp.currencyconverter.persistence.FileLoadManager;
import de.medieninf.mobcomp.currencyconverter.persistence.NetworkLoadManager;
import de.medieninf.mobcomp.currencyconverter.persistence.interfaces.LoadManager;
import de.medieninf.mobcomp.currencyconverter.util.DateUtil;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class CurrencyRateProviderImpl implements CurrencyRateProvider{

    private CurrencyRates currencyRates;
    private LoadManager lmDatabase;
    private LoadManager lmNetwork;
    private LoadManager lmFile;
    private DatabaseStoreManager smDatabase;
    private String referencedCurrency;

    public CurrencyRateProviderImpl(String referencedCurrency, SQLiteDatabase database, InputStream file, String url) {
        this.referencedCurrency = referencedCurrency;
        lmFile = new FileLoadManager(LoadManager.LoaderType.INIT, file);
        lmDatabase = new DatabaseLoadManager(LoadManager.LoaderType.DATABASE, database);
        lmNetwork = new NetworkLoadManager(LoadManager.LoaderType.NETWORK, url);

        lmNetwork.setSuccessor(lmDatabase);
        lmDatabase.setSuccessor(lmFile);

        smDatabase = new DatabaseStoreManager(database);
    }

    @Override
    public boolean updateRates(LoadManager.LoaderType type) throws Exception {
        CurrencyRates cr;
        // when to load data
        if(LoadManager.LoaderType.INIT == type || updateNeccessary()) {
            cr = lmNetwork.load(type);

            if (cr != null) {
                if(currencyRates != null) {
                    // checks if the timestamp of the new rates is different
                    Date lastUpdate = currencyRates.getTimestamp();
                    if (DateUtil.isSameDate(lastUpdate, cr.getTimestamp())) {
                        return false; // nothing changed
                    }
                }
                currencyRates = cr;
                // when to store data
                if(LoadManager.LoaderType.DATABASE != type) {
                    smDatabase.store(currencyRates);
                }
                return true;
            }
        }

        return false;
    }


    @Override
    public float getRate(final String currency) {
        float entryRate = 1;
        if(currencyRates != null) {
            CurrencyRateEntry entry = currencyRates.getRateEntry(currency);
            if(entry != null) {
                entryRate = entry.getRate();
            }
        }
        return entryRate;
    }

    @Override
    public List<CharSequence> getCurrencies() {
        List<CharSequence> list;
        list = currencyRates.getCurrencies();
        list.add(referencedCurrency);
        Collections.sort(list, new Comparator<CharSequence>() {
            @Override
            public int compare(CharSequence s1, CharSequence s2) {
                return s1.toString().compareToIgnoreCase(s2.toString());
            }
        });

        return list;
    }

    @Override
    public String getDate(String format) {
        if(format != null) {
            return DateUtil.formatDate(currencyRates.getTimestamp(), format);
        }
        return "";
    }

    private boolean updateNeccessary() {
        if(currencyRates != null) {
            final Date lastUpdate = currencyRates.getTimestamp();
            if(DateUtil.isToday(lastUpdate)) {
                return false;
            } else if(DateUtil.isWeekend()) {
                Date lastFriday = DateUtil.getLastFriday();
                return !DateUtil.isSameDate(lastUpdate, lastFriday);
            }
        }
        return true;
    }

}
