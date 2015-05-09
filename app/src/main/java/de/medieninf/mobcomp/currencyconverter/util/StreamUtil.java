package de.medieninf.mobcomp.currencyconverter.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by bchristiani on 10.05.2015.
 */
public final class StreamUtil {

    public static byte[] toByteArray(final InputStream in) throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final byte [] chunck = new byte[4096];
        int count;
        while ((count = in.read(chunck))>0) {
            outStream.write(chunck,0,count);
        }
        outStream.close();
        return outStream.toByteArray();
    }
}
