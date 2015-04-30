package de.medieninf.mobcomp.currencyconverter.test;

import android.test.InstrumentationTestCase;

/**
 * Created by bchristiani on 30.04.2015.
 */
public class FooTest extends InstrumentationTestCase{

    public void test() throws Exception{
        final int expected = 5;
        final int reality = 5;
        assertEquals(expected,reality);
    }
}
