package de.medieninf.mobcomp.currencyconverter.helper;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;
import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.exceptions.IllegalCurrencyException;
import de.medieninf.mobcomp.currencyconverter.helper.interfaces.Consumer;
import de.medieninf.mobcomp.currencyconverter.util.DateUtil;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class XMLConsumer implements Consumer{

    private static String TIMESTAMP_PATTERN = "EEE, dd MMMMM yyyy hh:mm:ss z";
    private static String TIMESTAMP_NODE = "lastBuildDate";
    private static String CURRENCY_NODE = "item";
    private static String TARGET_CURRENCY_NODE = "targetCurrency";
    private static String RATE_NODE = "exchangeRate";
    private static String NAME_NODE = "targetName";

    private static String TAG = XMLConsumer.class.getSimpleName();

    @Override
    public CurrencyRates parse(InputStream in) throws IllegalCurrencyException {
        CurrencyRates cr = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            cr = read(parser);
        } catch (XmlPullParserException | ParseException | IOException e) {
            e.printStackTrace();
            throw new IllegalCurrencyException(e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalCurrencyException(e.getMessage());
            }
        }
        return cr;
    }

    private CurrencyRates read(XmlPullParser parser) throws IOException, XmlPullParserException, ParseException {
        CurrencyRates cr = null;
        int eventType = parser.getEventType();
        CurrencyRateEntry entry = null;
        while(eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch(eventType) {
                case XmlPullParser.START_DOCUMENT:
                    cr = new CurrencyRates();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if(name.compareTo(CURRENCY_NODE)==0) {
                        entry = new CurrencyRateEntry();
                    } else if(name.compareTo(TARGET_CURRENCY_NODE)==0) {
                        entry.setCurrency(parser.nextText());
                    } else if(name.compareTo(RATE_NODE) == 0) {
                        entry.setRate(convertStringToFloat(parser.nextText()));
                    } else if(name.compareTo(NAME_NODE) == 0) {
                        entry.setName(parser.nextText());
                    } else if(name.compareTo(TIMESTAMP_NODE) == 0) {
                        Date timestamp = DateUtil.parseStringToDate(parser.nextText(), TIMESTAMP_PATTERN);
                        cr.setTimestamp(timestamp);
                    }
                   break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if(name.equalsIgnoreCase(CURRENCY_NODE) && entry != null) {
                        cr.addCurrencyRateEntry(entry);
                    }
            }
            eventType = parser.next();
        }
        return cr;
    }

    private float convertStringToFloat(final String value) {
        String val = value.replaceAll(",", ""); // in some cases is "," a separator for the thousends
        BigDecimal bd = new BigDecimal(val);
        return bd.floatValue();
    }
}
