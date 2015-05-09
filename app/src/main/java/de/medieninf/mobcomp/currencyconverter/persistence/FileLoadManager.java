package de.medieninf.mobcomp.currencyconverter.persistence;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.helper.XMLConsumer;
import de.medieninf.mobcomp.currencyconverter.helper.interfaces.Consumer;
import de.medieninf.mobcomp.currencyconverter.persistence.interfaces.LoadManager;
import de.medieninf.mobcomp.currencyconverter.util.StreamUtil;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class FileLoadManager extends LoadManager{

    private static final String TAG = FileLoadManager.class.getSimpleName();
    private Consumer consumer;
    private byte[] fileByteArray;

    public FileLoadManager(final String state, InputStream in) {
        super(state);
        this.consumer = new XMLConsumer();
        try {
            this.fileByteArray  = StreamUtil.toByteArray(in);
        } catch (Exception e) {
            Log.e(TAG, "Parsing Stream to ByteAray failed");
            e.printStackTrace();
        }
    }

    @Override
    public CurrencyRates load(final String state) {
        if(state.equals(this.state)) {
            CurrencyRates cr = null;
            try {
                cr = this.consumer.parse(new ByteArrayInputStream(fileByteArray));
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
