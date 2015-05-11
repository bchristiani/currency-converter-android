package de.medieninf.mobcomp.currencyconverter.persistence.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import de.medieninf.mobcomp.currencyconverter.persistence.db.schema.CurrencyRatesTbl;

/**
 * Created by bchristiani on 09.05.2015.
 */
public class CurrencyDatabaseHelper extends SQLiteOpenHelper{

    private static final String TAG = CurrencyDatabaseHelper.class.getSimpleName();
    private static final String DB_NAME = "currency.db";
    private static final int DB_VERSION = 1;
    private static CurrencyDatabaseHelper sINSTANCE;
    private static Object sLOCK = "";

    public static CurrencyDatabaseHelper getInstance(Context context) {
        if(sINSTANCE == null) {
            synchronized (sLOCK) {
                if(sINSTANCE == null && context != null) {
                    sINSTANCE = new CurrencyDatabaseHelper(context.getApplicationContext());
                }
            }
        }
        return sINSTANCE;
    }

    private CurrencyDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "onCreateTable");
        db.execSQL(CurrencyRatesTbl.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "onUpgradeTable");
        db.execSQL(CurrencyRatesTbl.SQL_DROP);
        onCreate(db);
    }
}
