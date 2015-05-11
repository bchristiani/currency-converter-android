package de.medieninf.mobcomp.currencyconverter.persistence.db.schema;

/**
 * Created by bchristiani on 11.05.2015.
 */
public interface CurrencyRateColums {

    /**
     * Primary Key.
     */
    String CURRENCY = "currency";

    String NAME = "name";

    String RATE = "rate";

    String TIMESTAMP = "timestamp";
}
