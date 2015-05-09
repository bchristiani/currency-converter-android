package de.medieninf.mobcomp.currencyconverter.persistence.interfaces;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;

/**
 * Created by bchristiani on 04.05.2015.
 */
public abstract class LoadManager {
    protected LoadManager lmSuccessor;
    protected String state;

    public LoadManager(final String state) {
        this.state = state;
    }

    public void setSuccessor(LoadManager successor)
    {
        lmSuccessor = successor;
    }

    public abstract CurrencyRates load(final String state);
}
