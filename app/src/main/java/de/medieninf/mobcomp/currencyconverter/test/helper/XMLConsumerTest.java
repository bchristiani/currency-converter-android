package de.medieninf.mobcomp.currencyconverter.test.helper;

import android.content.Context;
import android.content.res.Resources;
import android.test.InstrumentationTestCase;
import android.test.ServiceTestCase;
import android.test.mock.MockContext;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.lang.reflect.Method;

import de.medieninf.mobcomp.currencyconverter.R;
import de.medieninf.mobcomp.currencyconverter.activities.ConverterActivity;

/**
 * Created by bchristiani on 04.05.2015.
 */
public class XMLConsumerTest extends InstrumentationTestCase {

    private Resources res;

    @Override
    protected void setUp() throws Exception {
        res = getInstrumentation().getContext().getResources();
        assertNotNull(res);
    }

    public void test() throws Exception {
        InputStream in = res.openRawResource(R.raw.test);
        assertNotNull(in);
    }
}
