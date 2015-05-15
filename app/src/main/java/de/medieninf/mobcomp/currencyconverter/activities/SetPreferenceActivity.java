package de.medieninf.mobcomp.currencyconverter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by bchristiani on 12.05.2015.
 */
public class SetPreferenceActivity extends Activity {

    private static final String TAG = SetPreferenceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        AppPreferences appPreferences = new AppPreferences();
        if(bundle!=null)
            appPreferences.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                appPreferences).commit();
    }

}
