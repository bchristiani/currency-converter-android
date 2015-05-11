package de.medieninf.mobcomp.currencyconverter.persistence.interfaces;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.helper.XMLConsumer;
import de.medieninf.mobcomp.currencyconverter.helper.interfaces.Consumer;

/**
 * Created by bchristiani on 04.05.2015.
 */
public abstract class LoadManager {
    protected LoadManager lmSuccessor;
    protected LoaderType type;
    protected Consumer consumer;

    public LoadManager(LoaderType type) {
        this.type = type;
        this.consumer = new XMLConsumer();
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
