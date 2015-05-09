package de.medieninf.mobcomp.currencyconverter.logic;

import android.database.sqlite.SQLiteDatabase;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
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

    public CurrencyRateProviderImpl(String referencedCurrency, SQLiteDatabase database, InputStream file) {
        this.referencedCurrency = referencedCurrency;
        lmFile = new FileLoadManager("init", file);
        lmDatabase = new DatabaseLoadManager("database", database);
        lmNetwork = new NetworkLoadManager("network");

        lmNetwork.setSuccessor(lmDatabase);
        lmDatabase.setSuccessor(lmFile);

        smDatabase = new DatabaseStoreManager(database);
    }

    @Override
    public boolean updateRates(final String state) {
        currencyRates = lmNetwork.load(state);
        if(currencyRates != null && state.compareTo("database")!=0) {
            smDatabase.store(currencyRates);
            return true;
        }
        return false;
    }

    @Override
    public float getRate(final String currency) {
        float entry = 0;
        if(currencyRates != null) {
            entry = currencyRates.getRateEntry(currency).getRate();
        }
        return entry;
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
    public String getDate() {
        return DateUtil.formatDate(currencyRates.getTimestamp(), "dd.MM.yyyy");
    }

}
