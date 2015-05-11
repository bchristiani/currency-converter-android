package de.medieninf.mobcomp.currencyconverter.persistence.db.schema;

/**
 * Created by bchristiani on 11.05.2015.
 */
public final class CurrencyRatesTbl implements CurrencyRateColums {
    /**
     * Name of the table
     */
    public static final String TABLE_NAME = "currencies";

    public static final String SQL_CREATE =
            "CREATE TABLE " +
             TABLE_NAME +
            " (" +
            CURRENCY +
            " TEXT PRIMARY KEY NOT NULL, " +
            RATE +
            " REAL NOT NULL, " +
            NAME +
            " TEXT NOT NULL, " +
            TIMESTAMP +
            " TEXT NOT NULL)";

    public static final String SQL_DROP =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd";

    public static final String[] TABLE_ATTRIBUTES = {"currency", "rate", "name", "timestamp"};

    private CurrencyRatesTbl() {

    }
}
