package de.medieninf.mobcomp.currencyconverter.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import de.medieninf.mobcomp.currencyconverter.R;
import de.medieninf.mobcomp.currencyconverter.exceptions.NetworkConnectionException;
import de.medieninf.mobcomp.currencyconverter.logic.CurrencyRateProviderImpl;
import de.medieninf.mobcomp.currencyconverter.logic.interfaces.CurrencyRateProvider;
import de.medieninf.mobcomp.currencyconverter.persistence.db.CurrencyDatabaseHelper;
import de.medieninf.mobcomp.currencyconverter.persistence.db.schema.CurrencyRatesTbl;
import de.medieninf.mobcomp.currencyconverter.persistence.interfaces.LoadManager;
import de.medieninf.mobcomp.currencyconverter.util.CurrencyConverterUtil;


public class ConverterActivity extends ActionBarActivity {
    //declare static variables
    private final static String TAG = ConverterActivity.class.getSimpleName();
    private final static String AMOUNT_INPUT_KEY = "amountInput";
    private final static String START_CURRENCY_KEY = "startCurrency";
    private final static String TARGET_CURRENCY_KEY = "targetCurrency";
    // declare widgets
    private Button btnReset;
    private Toast toastError;
    private Toast toastRatesUpToDate;
    private Spinner spinnerStartCurrency;
    private Spinner spinnerTargetCurrency;
    private EditText etStartAmount;
    private EditText etTargetAmount;
    private TextView tvTimestamp;
    private CurrencyRateProvider currencyRateProvider;
    private String referencedCurrency;
    private ProgressDialog pDialog;
    // declare listener
    private View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "onResetListener");
            TextKeyListener.clear(etStartAmount.getText());
        }
    };
    private TextWatcher twStartAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.v(TAG, "afterTextChanged ");
            setText(etTargetAmount, selectedStartCurrency, selectedTargetCurrency, etStartAmount.getText().toString());
        }
    };
    private TextWatcher twTargetAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.v(TAG, "afterTextChanged");
            setText(etStartAmount, selectedTargetCurrency, selectedStartCurrency, etTargetAmount.getText().toString());
        }
    };
    private AdapterView.OnItemSelectedListener avoisl = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.v(TAG, "onItemSelected");
            final String selected = (String) parent.getSelectedItem();
            final int parentId = parent.getId();
            if (spinnerStartCurrency != null && parentId == spinnerStartCurrency.getId()) {
                selectedStartCurrency = selected;
            } else if (spinnerTargetCurrency != null && parentId == spinnerTargetCurrency.getId()) {
                selectedTargetCurrency = selected;
            }

            currencyChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.v(TAG, "onNothingSelected");
        }
    };
    // declare other variables
    private Resources res;
    private SharedPreferences prefs;
    private String tvDate;
    private String selectedStartCurrency;
    private String selectedTargetCurrency;
    private boolean setTextFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setContentView(R.layout.activity_converter);
        preInit();
        new InitTask().execute();
    }

    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "onCreateOptionMenu");
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu_converter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");
        switch(item.getItemId()) {
            case R.id.action_update:
                pDialog = ProgressDialog.show(this, "Bitte Warten", "Lade Kursdaten...", true);
                new UpdateTask().execute(LoadManager.LoaderType.NETWORK);
                return true;
            case R.id.action_revert:
                pDialog = ProgressDialog.show(this, "Bitte Warten", "Lade Kursdaten...", true);
                new UpdateTask().execute(LoadManager.LoaderType.INIT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
        if(prefs != null) {
            final SharedPreferences.Editor editor = prefs.edit();
            editor.putString(AMOUNT_INPUT_KEY,etStartAmount.getText().toString());
            editor.putString(START_CURRENCY_KEY,selectedStartCurrency);
            editor.putString(TARGET_CURRENCY_KEY,selectedTargetCurrency);
            editor.commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
      protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    private void preInit() {
        Log.v(TAG, "preInit");
        res = getResources();
        referencedCurrency = res.getString(R.string.reference_currency);
        tvDate = getResources().getString(R.string.tv_date);
        // instantiate widgets
        pDialog = ProgressDialog.show(this, res.getString(R.string.progress_headline), res.getString(R.string.progress_text), true);
        btnReset = (Button) findViewById(R.id.btn_reset);
        spinnerStartCurrency = (Spinner) findViewById(R.id.spinner_start_currency);
        spinnerTargetCurrency = (Spinner) findViewById(R.id.spinner_target_currency);
        etStartAmount = (EditText) findViewById(R.id.et_start_amount);
        etStartAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etTargetAmount = (EditText) findViewById(R.id.et_target_amount);
        tvTimestamp = (TextView) findViewById(R.id.tv_timestamp);
        toastRatesUpToDate = Toast.makeText(ConverterActivity.this, R.string.msg_rates_up_to_date, Toast.LENGTH_SHORT);
        toastError = Toast.makeText(ConverterActivity.this, R.string.error_msg_network, Toast.LENGTH_SHORT);
        etTargetAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // set listener
        btnReset.setOnClickListener(resetListener);
        etStartAmount.addTextChangedListener(twStartAmount);
        etTargetAmount.addTextChangedListener(twTargetAmount);
        spinnerStartCurrency.setOnItemSelectedListener(avoisl);
        spinnerTargetCurrency.setOnItemSelectedListener(avoisl);
    }

    private void postInit() {
        Log.v(TAG,"postInit");

        // load saved user inputs
        prefs = getPreferences(MODE_PRIVATE);
        final String startCurrency = prefs.getString(START_CURRENCY_KEY,null);
        final String targetCurrency = prefs.getString(TARGET_CURRENCY_KEY,null);
        final String inputAmount = prefs.getString(AMOUNT_INPUT_KEY,null);

        // set adapter
        ArrayAdapter<CharSequence> adapterStartCurrency = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyRateProvider.getCurrencies());
        adapterStartCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartCurrency.setAdapter(adapterStartCurrency);
        if(startCurrency != null) {
            int posStartCurrency = adapterStartCurrency.getPosition(startCurrency);
            if(posStartCurrency >= 0)
                spinnerStartCurrency.setSelection(posStartCurrency);
        }

        ArrayAdapter<CharSequence> adapterTargetCurrency = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyRateProvider.getCurrencies());
        adapterTargetCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTargetCurrency.setAdapter(adapterTargetCurrency);
        if(targetCurrency != null) {
            int posTargetCurrency = adapterTargetCurrency.getPosition(targetCurrency);
            if(posTargetCurrency >= 0)
                spinnerTargetCurrency.setSelection(posTargetCurrency);
        }

        refreshGUI(inputAmount);
    }

    private void refreshGUI(final String startAmount) {
        Log.v(TAG, "refreshGUI");
        tvTimestamp.setText(tvDate.concat(currencyRateProvider.getDate(res.getString(R.string.rate_date_format))));
        if(!startAmount.isEmpty())
            etStartAmount.setText(startAmount);
        if(pDialog!=null) {
            pDialog.dismiss();
        }
    }

    private void currencyChanged() {
        if (selectedStartCurrency != null && selectedTargetCurrency != null) {
            final String startAmount = etStartAmount.getText().toString();
            final String targetAmount = etTargetAmount.getText().toString();
            if (!startAmount.isEmpty()) {
                setText(etTargetAmount, selectedStartCurrency, selectedTargetCurrency, etStartAmount.getText().toString());
            } else if (!targetAmount.isEmpty()) {
                setText(etStartAmount, selectedTargetCurrency, selectedStartCurrency, etTargetAmount.getText().toString());
            }
        }
    }

    private void setText(EditText etWidget, String startCurrency, String targetCurrency, String amount) {
        if (setTextFlag) {
            setTextFlag = false;
        } else {
            if (startCurrency != null || targetCurrency != null) {
                setTextFlag = true;
                String result = calculateCurrency(startCurrency, targetCurrency, amount);
                if (result.isEmpty()) {
                    TextKeyListener.clear(etWidget.getText());
                } else {
                    etWidget.setText(result);
                }
            }
        }
    }

    private String calculateCurrency(String startCurrency, String targetCurrency, String amount) {
        String result = "";
        if (amount.length() > 0 && CurrencyConverterUtil.isNumeric(amount)) {
            if (startCurrency.compareTo(targetCurrency) == 0) {
                result = CurrencyConverterUtil.convertOtherCurrency(amount, 1, 1);
            } else if (startCurrency.compareTo(referencedCurrency) == 0 && targetCurrency.compareTo(referencedCurrency) != 0) {
                result = CurrencyConverterUtil.convertEuroCurrency(amount, CurrencyConverterUtil.Type.EURO_TO_OTHER, currencyRateProvider.getRate(targetCurrency));
            } else if (startCurrency.compareTo(referencedCurrency) != 0 && targetCurrency.compareTo(referencedCurrency) != 0) {
                result = CurrencyConverterUtil.convertOtherCurrency(amount, currencyRateProvider.getRate(startCurrency), currencyRateProvider.getRate(targetCurrency));
            } else if (startCurrency.compareTo(referencedCurrency) != 0 && targetCurrency.compareTo(referencedCurrency) == 0) {
                result = CurrencyConverterUtil.convertEuroCurrency(amount, CurrencyConverterUtil.Type.OTHER_TO_EURO, currencyRateProvider.getRate(startCurrency));
            }
        }
        return result;
    }

    private void updateCurrencyRates(LoadManager.LoaderType type) {
        try {
            boolean updateNeeded = currencyRateProvider.updateRates(type);
            if(updateNeeded == false) {
                toastRatesUpToDate.show();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            if(e instanceof NetworkConnectionException) {
                toastError.show();
            } else {
                finish();
            }
        }
    }
    // ####################################### INNER CLASS #######################################
    private class InitTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            // instantiate database
            CurrencyDatabaseHelper dbHelper = CurrencyDatabaseHelper.getInstance(ConverterActivity.this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long count = DatabaseUtils.queryNumEntries(db, CurrencyRatesTbl.TABLE_NAME);
            InputStream defaultCurrencyXml = getResources().openRawResource(R.raw.euro_currency_rates);
            final String currencyUrl = getResources().getString(R.string.currency_url_ezb);
            currencyRateProvider = new CurrencyRateProviderImpl(referencedCurrency, db, defaultCurrencyXml, currencyUrl);
            LoadManager.LoaderType type;
            if(count == 0) { // load rates from xml file
                type = LoadManager.LoaderType.INIT;
            } else { // load rates from sqlite database
                type = LoadManager.LoaderType.DATABASE;
            }
            updateCurrencyRates(type);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            postInit();
        }
    }

    private class UpdateTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            Log.v(TAG, "Start update rates...");
            if(params.length > 0) {
                LoadManager.LoaderType type = (LoadManager.LoaderType)params[0];
                updateCurrencyRates(type);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.v(TAG, "End update rates");
            super.onPostExecute(o);
            refreshGUI(etStartAmount.getText().toString());
        }
    }
}

