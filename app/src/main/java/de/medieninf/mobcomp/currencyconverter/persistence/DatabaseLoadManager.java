package de.medieninf.mobcomp.currencyconverter.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;
import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.helper.interfaces.Consumer;
import de.medieninf.mobcomp.currencyconverter.persistence.interfaces.LoadManager;
import de.medieninf.mobcomp.currencyconverter.util.DateUtil;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class DatabaseLoadManager extends LoadManager{

    private static final String TAG = DatabaseLoadManager.class.getSimpleName();
    private SQLiteDatabase database;

    public DatabaseLoadManager(String state, SQLiteDatabase database) {
        super(state);
        this.database = database;
    }

    @Override
    public CurrencyRates load(final String state) {
        if(state.equals(this.state)) {
            return readInformationFromDatabase();
        } else {
            return this.lmSuccessor.load(state);
        }
    }

    private CurrencyRates readInformationFromDatabase() {
        Log.v(TAG, "readInformationFromDatabase");
        CurrencyRates cr = new CurrencyRates();
        String[] projection = {"currency", "rate", "timestamp"};
        Cursor c = database.query("currencies", projection, null, null, null, null, null);
        c.moveToFirst();
        String timestamp = "";
        while(c.moveToNext()) {
            CurrencyRateEntry entry = new CurrencyRateEntry(c.getString(0),c.getFloat(1));
            cr.addCurrencyRateEntry(entry);
            timestamp = c.getString(2);
        }
        c.close();
        try {
            cr.setTimestamp(DateUtil.parseStringToDate(timestamp, "yyyy-MM-dd"));
        } catch (ParseException e) {
            //TODO catch something
            e.printStackTrace();
        }
        return cr;
    }
}
