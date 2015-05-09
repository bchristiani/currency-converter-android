package de.medieninf.mobcomp.currencyconverter.persistence;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.persistence.interfaces.LoadManager;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class NetworkLoadManager extends LoadManager{

    public NetworkLoadManager(String state) {
        super(state);
    }

    @Override
    public CurrencyRates load(String state) {
        if(state.equals(this.state)) {
            return null;
        } else {
            return this.lmSuccessor.load(state);
        }
    }
}
