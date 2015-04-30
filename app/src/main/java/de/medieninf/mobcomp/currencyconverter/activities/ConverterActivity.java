package de.medieninf.mobcomp.currencyconverter.activities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import de.medieninf.mobcomp.currencyconverter.R;


public class ConverterActivity extends Activity {

    private Spinner spinnerStartCurrency;
    private Spinner spinnerTargetCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        spinnerStartCurrency = (Spinner) findViewById(R.id.spinner_start_currency);
        spinnerTargetCurrency = (Spinner) findViewById(R.id.spinner_target_currency);

        ArrayAdapter<CharSequence> adapterStartCurrency = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapterStartCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartCurrency.setAdapter(adapterStartCurrency);

        ArrayAdapter<CharSequence> adapterTargetCurrency = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapterTargetCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTargetCurrency.setAdapter(adapterTargetCurrency);
    }
}
