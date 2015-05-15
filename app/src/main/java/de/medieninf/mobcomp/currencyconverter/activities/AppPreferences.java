package de.medieninf.mobcomp.currencyconverter.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import de.medieninf.mobcomp.currencyconverter.R;

/**
 * Created by bchristiani on 11.05.2015.
 */
public class AppPreferences extends PreferenceFragment{

    private static final String TAG = AppPreferences.class.getSimpleName();
    private static String CHECKBOX_PREFERENCES;
    private static String DEFAULT_AMOUNT_PREFERENCES;
    private static String DEFAULT_START_CURRENCY_PREFERENCES;
    private static String DEFAULT_TARGET_CURRENCY_PREFERENCES;
    private static String DECIMAL_PLACES_PREFERENCES;
    private Resources res;
    private SharedPreferences sharedPreferences;
    //declare fields
    CheckBoxPreference checkbox;
    EditTextPreference etpAmount;
    ListPreference lpDecimalPlaces;
    ListPreference lpStartCurrency;
    ListPreference lpTargetCurrency;

    Preference.OnPreferenceChangeListener onSummaryListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if(newValue!=null) {
                preference.setSummary((String) newValue);
                return true;
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = getActivity().getSharedPreferences("mySharedPrefs", Context.MODE_PRIVATE);
        res = getResources();
        CHECKBOX_PREFERENCES = res.getString(R.string.checkbox_preference);
        DEFAULT_AMOUNT_PREFERENCES = res.getString(R.string.default_amount_preference);
        DECIMAL_PLACES_PREFERENCES = res.getString(R.string.decimal_places_preference);
        DEFAULT_START_CURRENCY_PREFERENCES = res.getString(R.string.default_start_currency_preference);
        DEFAULT_TARGET_CURRENCY_PREFERENCES = res.getString(R.string.default_target_currency_preference);
        // instantiate fields
        checkbox = (CheckBoxPreference)findPreference(CHECKBOX_PREFERENCES);
        etpAmount = (EditTextPreference)findPreference(DEFAULT_AMOUNT_PREFERENCES);
        lpDecimalPlaces = (ListPreference)findPreference(DECIMAL_PLACES_PREFERENCES);
        lpStartCurrency = (ListPreference)findPreference(DEFAULT_START_CURRENCY_PREFERENCES);
        lpTargetCurrency = (ListPreference)findPreference(DEFAULT_TARGET_CURRENCY_PREFERENCES);

        CharSequence [] currencies = {};
        Bundle bundle = getArguments();
        if(bundle!=null) {
            CharSequence[] c = bundle.getCharSequenceArray("currencies");
            if(c!=null) {
                currencies = c;
            }
        }

        lpStartCurrency.setEntries(currencies);
        lpStartCurrency.setEntryValues(currencies);
        lpTargetCurrency.setEntryValues(currencies);
        lpTargetCurrency.setEntries(currencies);

        // set listener
        checkbox.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.v(TAG, "onPreferenceChange");
                boolean val = (boolean) newValue;
                CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                if(val) {
                    checkBoxPreference.setTitle("AN");
                }else {
                    checkBoxPreference.setTitle("AUS");
                }
                return true;
            }
        });
        lpDecimalPlaces.setOnPreferenceChangeListener(onSummaryListener);
        lpStartCurrency.setOnPreferenceChangeListener(onSummaryListener);
        lpTargetCurrency.setOnPreferenceChangeListener(onSummaryListener);
        etpAmount.setOnPreferenceChangeListener(onSummaryListener);
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        if(checkbox.isChecked()) {
            checkbox.setTitle("AN");
        } else {
            checkbox.setTitle("AUS");
        }

        etpAmount.setSummary(etpAmount.getText());
        lpDecimalPlaces.setSummary(lpDecimalPlaces.getValue());
        lpStartCurrency.setSummary(lpStartCurrency.getValue());
        lpTargetCurrency.setSummary(lpTargetCurrency.getValue());
    }

    @Override
    public void onPause() {
        Log.v(TAG, "onPause");
        super.onPause();
        if(sharedPreferences!=null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(CHECKBOX_PREFERENCES, checkbox.isChecked());
            editor.putString(DECIMAL_PLACES_PREFERENCES, lpDecimalPlaces.getValue());
            editor.putString(DEFAULT_AMOUNT_PREFERENCES, etpAmount.getText());
            editor.putString(DEFAULT_START_CURRENCY_PREFERENCES, lpStartCurrency.getValue());
            editor.putString(DEFAULT_TARGET_CURRENCY_PREFERENCES, lpTargetCurrency.getValue());
            editor.commit();
        }
    }
}
