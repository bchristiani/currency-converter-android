package de.medieninf.mobcomp.currencyconverter.exceptions;

/**
 * Created by bchristiani on 11.05.2015.
 */
public class NetworkConnectionException extends Exception {
    //Parameterless Constructor
    public NetworkConnectionException() {}

    //Constructor that accepts a message
    public NetworkConnectionException(String message)
    {
        super(message);
    }
}
