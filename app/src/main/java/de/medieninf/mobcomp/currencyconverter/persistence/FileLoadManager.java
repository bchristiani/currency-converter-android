package de.medieninf.mobcomp.currencyconverter.persistence;

import java.io.IOException;
import java.io.InputStream;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.helper.XMLConsumer;
import de.medieninf.mobcomp.currencyconverter.helper.interfaces.Consumer;
import de.medieninf.mobcomp.currencyconverter.persistence.interfaces.LoadManager;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class FileLoadManager extends LoadManager{

    private Consumer consumer;
    private InputStream in;

    public FileLoadManager(final String state, InputStream in) {
        super(state);
        this.consumer = new XMLConsumer();
        this.in = in;
    }

    @Override
    public CurrencyRates load(final String state) {
        if(state.equals(this.state)) {
            CurrencyRates cr = null;
            try {
                cr = this.consumer.parse(in);
            } catch (IOException e) {
                // TODO: Log statement
                e.printStackTrace();
            }
            return cr;
        } else {
            return this.lmSuccessor.load(state);
        }
    }
}
