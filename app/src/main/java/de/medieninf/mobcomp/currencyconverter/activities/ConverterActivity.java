package de.medieninf.mobcomp.currencyconverter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import de.medieninf.mobcomp.currencyconverter.R;
import de.medieninf.mobcomp.currencyconverter.utils.ConverterUtil;


public class ConverterActivity extends Activity {

    private static String TAG = ConverterActivity.class.getSimpleName();

    private Spinner spinnerStartCurrency;
    private Spinner spinnerTargetCurrency;
    private Button btnCalculate;
    private TextView tvResult;
    private EditText etAmount;
    private Toast toast;
    private String selectedStartCurrency;
    private String selectedTargetCurrency;

    private View.OnClickListener onCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickCalculate();
        }
    };

    // TODO: eigene Klasse
    private AdapterView.OnItemSelectedListener avoisl = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.v(TAG,"onItemSelected");
            final String selected = (String) parent.getSelectedItem();
            final int parentId = parent.getId();
            if (spinnerStartCurrency!=null && parentId == spinnerStartCurrency.getId()) {
                selectedStartCurrency = selected;
            } else if(spinnerTargetCurrency!=null && parentId == spinnerTargetCurrency.getId()) {
                selectedTargetCurrency = selected;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Log.v(TAG,"onNothingSelected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
        setContentView(R.layout.activity_converter);

        // instantiate widgets
        spinnerStartCurrency = (Spinner) findViewById(R.id.spinner_start_currency);
        spinnerTargetCurrency = (Spinner) findViewById(R.id.spinner_target_currency);
        btnCalculate = (Button) findViewById(R.id.btn_calculate);
        tvResult = (TextView) findViewById(R.id.tv_result);
        etAmount = (EditText) findViewById(R.id.et_amount);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        toast = Toast.makeText(ConverterActivity.this,R.string.toast_hint, Toast.LENGTH_SHORT);

        // set adapter
        ArrayAdapter<CharSequence> adapterStartCurrency = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapterStartCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartCurrency.setAdapter(adapterStartCurrency);

        ArrayAdapter<CharSequence> adapterTargetCurrency = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapterTargetCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTargetCurrency.setAdapter(adapterTargetCurrency);

        // set listener
        btnCalculate.setOnClickListener(onCalculateListener);
        spinnerStartCurrency.setOnItemSelectedListener(avoisl);
        spinnerTargetCurrency.setOnItemSelectedListener(avoisl);
    }

    private void onClickCalculate() {
        Log.v(TAG, "onClickCalculate");
        String etAmountText = etAmount.getText().toString();
        String result = "";
        //TODO: Check if input text is a number within Util class.
        if(etAmountText.length() > 0 && ConverterUtil.isNumeric(etAmountText)) {
            if (selectedStartCurrency.compareTo(selectedTargetCurrency) == 0) {
                result = ConverterUtil.convertOtherCurrency(etAmountText, 1, 1);
            } else if (selectedStartCurrency.compareTo("EUR")==0 && selectedTargetCurrency.compareTo("EUR") != 0) {
                result = ConverterUtil.convertEuroCurrency(etAmountText, ConverterUtil.Type.EURO_TO_OTHER, getRate(selectedTargetCurrency));
            } else if(selectedStartCurrency.compareTo("EUR")!=0 && selectedTargetCurrency.compareTo("EUR") != 0) {
                result = ConverterUtil.convertOtherCurrency(etAmountText,getRate(selectedStartCurrency),getRate(selectedTargetCurrency));
            } else if(selectedStartCurrency.compareTo("EUR") !=0 && selectedTargetCurrency.compareTo("EUR") == 0) {
                result = ConverterUtil.convertEuroCurrency(etAmountText, ConverterUtil.Type.OTHER_TO_EURO, getRate(selectedStartCurrency));
            }
            // set content of Text View Widget for the result
            String formattedResult = ConverterUtil.getFormattedAmount(result,selectedTargetCurrency);
            tvResult.setText(formattedResult);
        } else {
            toast.show();
        }
    }

    private float getRate(final String currency) {
        TypedValue outValue = new TypedValue();
        int id;
        if(currency.compareTo("USD")==0) {
            id = R.dimen.usd_rate;
        } else if(currency.compareTo("GRD") == 0) {
            id = R.dimen.grd_rate;
        } else {
            id = R.dimen.eur_rate;
        }
        getResources().getValue(id,outValue,true);
        return outValue.getFloat();
    }
}
