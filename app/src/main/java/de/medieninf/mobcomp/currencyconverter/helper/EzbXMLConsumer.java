package de.medieninf.mobcomp.currencyconverter.helper;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;

import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRateEntry;
import de.medieninf.mobcomp.currencyconverter.entities.CurrencyRates;
import de.medieninf.mobcomp.currencyconverter.exceptions.IllegalCurrencyException;
import de.medieninf.mobcomp.currencyconverter.helper.interfaces.Consumer;
import de.medieninf.mobcomp.currencyconverter.util.DateUtil;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class EzbXMLConsumer implements Consumer{

    private static String TIMESTAMP_PATTERN = "yyyy-MM-dd";
    private static String CURRENCY_NODE = "Cube";
    private static String TIMESTAMP_ATTRIBUTE = "time";
    private static String CURRENCY_ATTRIBUTE = "currency";
    private static String RATE_ATTRIBUTE = "rate";
    private static int COUNT_ATTRIBUTES = 2;

    private static String TAG = EzbXMLConsumer.class.getSimpleName();

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
        CurrencyRates cr = new CurrencyRates();
        int eventType = parser.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT) {
            if(eventType == XmlPullParser.START_TAG) {
                String nodeName = parser.getName();
                int countAttribute = parser.getAttributeCount();
                if (nodeName.equals(CURRENCY_NODE)) {
                    if(countAttribute == COUNT_ATTRIBUTES-1) {
                        final String timestampValue = parser.getAttributeValue(null, TIMESTAMP_ATTRIBUTE);
                        cr.setTimestamp(DateUtil.parseStringToDate(timestampValue, TIMESTAMP_PATTERN));
                    } else if(countAttribute == COUNT_ATTRIBUTES) {
                        final String currencyValue = parser.getAttributeValue(null, CURRENCY_ATTRIBUTE);
                        final String rateValue = parser.getAttributeValue(null, RATE_ATTRIBUTE);
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
