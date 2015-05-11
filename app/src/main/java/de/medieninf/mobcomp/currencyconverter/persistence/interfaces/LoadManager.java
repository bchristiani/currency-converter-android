package de.medieninf.mobcomp.currencyconverter.persistence.interfaces;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;

/**
 * Created by bchristiani on 04.05.2015.
 */
public abstract class LoadManager {
    protected LoadManager lmSuccessor;
    protected LoaderType type;

    public LoadManager(LoaderType type) {
        this.type = type;
    }

    public void setSuccessor(LoadManager successor)
    {
        lmSuccessor = successor;
    }

    public abstract CurrencyRates load(LoaderType type) throws Exception;

    public static enum LoaderType {
        INIT,DATABASE,NETWORK;
    }
}
