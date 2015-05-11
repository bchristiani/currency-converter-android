package de.medieninf.mobcomp.currencyconverter.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;
import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.helper.interfaces.Consumer;
import de.medieninf.mobcomp.currencyconverter.persistence.db.schema.CurrencyRatesTbl;
import de.medieninf.mobcomp.currencyconverter.persistence.interfaces.LoadManager;
import de.medieninf.mobcomp.currencyconverter.util.DateUtil;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class DatabaseLoadManager extends LoadManager{

    private static final String TAG = DatabaseLoadManager.class.getSimpleName();
    private SQLiteDatabase database;

    public DatabaseLoadManager(LoaderType type, SQLiteDatabase database) {
        super(type);
        this.database = database;
    }

    @Override
    public CurrencyRates load(LoaderType type) throws Exception{
        if(type == this.type) {
            return readInformationFromDatabase();
        } else {
            return this.lmSuccessor.load(type);
        }
    }

    private CurrencyRates readInformationFromDatabase() {
        Log.v(TAG, "readInformationFromDatabase");
        CurrencyRates cr = new CurrencyRates();
        String[] projection = CurrencyRatesTbl.TABLE_ATTRIBUTES;
        Cursor c = database.query(CurrencyRatesTbl.TABLE_NAME, projection, null, null, null, null, null);
        c.moveToFirst();
        String timestamp = "";
        while(c.moveToNext()) {
            CurrencyRateEntry entry = new CurrencyRateEntry(c.getString(0),c.getString(2), c.getFloat(1));
            cr.addCurrencyRateEntry(entry);
            timestamp = c.getString(3);
        }
        c.close();
        try {
            cr.setTimestamp(DateUtil.parseStringToDate(timestamp, CurrencyRatesTbl.TIMESTAMP_FORMAT));
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return cr;
    }
}
