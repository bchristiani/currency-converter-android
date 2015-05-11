package de.medieninf.mobcomp.currencyconverter.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.exceptions.NetworkConnectionException;
import de.medieninf.mobcomp.currencyconverter.helper.XMLConsumer;
import de.medieninf.mobcomp.currencyconverter.helper.interfaces.Consumer;
import de.medieninf.mobcomp.currencyconverter.persistence.interfaces.LoadManager;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class NetworkLoadManager extends LoadManager{

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final String REQUEST_METHOD = "GET";
    private String url;
    private Consumer consumer;

    public NetworkLoadManager(LoaderType type, String url) {
        super(type);
        this.url = url;
        this.consumer = new XMLConsumer();
    }

    @Override
    public CurrencyRates load(LoaderType type) throws Exception{
        if(type == this.type) {
            InputStream stream = null;
            CurrencyRates cr = new CurrencyRates();
            try{
                stream = downloadUrl(url);
                cr = consumer.parse(stream);
            } catch (IOException e) {
                e.printStackTrace();
                throw new NetworkConnectionException(e.getMessage());
            } finally {
                if(stream!=null) {
                    stream.close();
                }
            }
            return cr;
        } else {
            return this.lmSuccessor.load(type);
        }
    }

    private InputStream downloadUrl(final String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(READ_TIMEOUT);
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setRequestMethod(REQUEST_METHOD);
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
}
