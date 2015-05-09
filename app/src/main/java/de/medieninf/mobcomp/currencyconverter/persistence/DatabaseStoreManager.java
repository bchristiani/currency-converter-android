package de.medieninf.mobcomp.currencyconverter.persistence;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;
import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.persistence.db.CurrencyDatabaseHelper;
import de.medieninf.mobcomp.currencyconverter.util.DateUtil;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class DatabaseStoreManager {

    private static final String TAG = DatabaseStoreManager.class.getSimpleName();
    private SQLiteDatabase database;
    private CurrencyRates currencyRates;

    public DatabaseStoreManager(SQLiteDatabase database) {
        this.database = database;
    }

    public void store(CurrencyRates cr) {
        currencyRates = cr;
        new UpdateDatabaseTask().execute();
    }

    private class UpdateDatabaseTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            Log.v(TAG, "start updateDatabase");
            // insert or update row into database
            ContentValues args = new ContentValues();
            final String timestamp = DateUtil.formatDate(currencyRates.getTimestamp(), "yyyy-MM-dd");
            for (CurrencyRateEntry entry: currencyRates.getCurrencyRateEntries()) {
                args.put("currency", entry.getCurrency());
                args.put("rate", entry.getRate());
                args.put("timestamp", timestamp);
                database.insertWithOnConflict("currencies", null, args, SQLiteDatabase.CONFLICT_REPLACE);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.v(TAG, "end updateDatabase");
        }
    }
}
