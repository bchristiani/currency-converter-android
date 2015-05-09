package de.medieninf.mobcomp.currencyconverter.helper;

import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;
import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.helper.interfaces.Consumer;
import de.medieninf.mobcomp.currencyconverter.util.DateUtil;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class XMLConsumer implements Consumer{

    private static String TAG = XMLConsumer.class.getSimpleName();
    private static final String ns = null;

    @Override
    public CurrencyRates parse(InputStream in) throws IOException {
        CurrencyRates cr = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            cr = read(parser);
        } catch (XmlPullParserException e) {
            Log.e(TAG, "XxmlPullParserException");
            e.printStackTrace();
        } finally {
            in.close();
        }
        return cr;
    }

    private CurrencyRates read(XmlPullParser parser) throws IOException, XmlPullParserException {
        CurrencyRates cr = new CurrencyRates();
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                //TODO: Strings into resource file
                String nodeName = parser.getName();
                int countAttribute = parser.getAttributeCount();
                if (nodeName.equals("Cube")) {
                    if(countAttribute == 1) {
                        final String timestampValue = parser.getAttributeValue(null, "time");
                        try {
                            cr.setTimestamp(DateUtil.parseStringToDate(timestampValue, "yyyy-MM-dd"));
                        } catch (ParseException e) {
                            Log.e(TAG, "ParceException while parsing timestamp.");
                            e.printStackTrace();
                        }
                    } else if(countAttribute == 2) {
                        final String currencyValue = parser.getAttributeValue(null, "currency");
                        final String rateValue = parser.getAttributeValue(null, "rate");
                        CurrencyRateEntry entry = new CurrencyRateEntry(currencyValue, convertStringToFloat(rateValue));
                        cr.addCurrencyRateEntry(entry);

                    }
                }
            }
            eventType = parser.next();
        }
        return cr;
    }

    private float convertStringToFloat(final String val) {
        BigDecimal bd = new BigDecimal(val);
        return bd.floatValue();
    }
}
