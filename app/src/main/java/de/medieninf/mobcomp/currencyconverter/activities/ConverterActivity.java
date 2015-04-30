package de.medieninf.mobcomp.currencyconverter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
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

    private View.OnClickListener onCalculateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickCalculate();
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

        // set listener
        btnCalculate.setOnClickListener(onCalculateListener);

        // set adapter
        ArrayAdapter<CharSequence> adapterStartCurrency = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapterStartCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartCurrency.setAdapter(adapterStartCurrency);

        ArrayAdapter<CharSequence> adapterTargetCurrency = ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapterTargetCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTargetCurrency.setAdapter(adapterTargetCurrency);
    }

    private void onClickCalculate() {
        Log.v(TAG, "onClickCalculate");

        String etAmountText = etAmount.getText().toString();
        String result = "";
        //TODO: Check if input text is a number within Util class.
        if(etAmountText.length() > 0) {
            result = ConverterUtil.convertEuroCurrency(etAmountText, ConverterUtil.Type.EURO_TO_OTHER, 1.11f);
        } else {
            toast.show();
        }
        // set content of Text View Widget for the result
        tvResult.setText(result);
    }
}
