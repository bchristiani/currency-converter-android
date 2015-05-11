package de.medieninf.mobcomp.currencyconverter.exceptions;

/**
 * Created by bchristiani on 11.05.2015.
 */
public class IllegalCurrencyException extends Exception {
    //Parameterless Constructor
    public IllegalCurrencyException() {}

    //Constructor that accepts a message
    public IllegalCurrencyException(String message)
    {
        super(message);
    }
}
