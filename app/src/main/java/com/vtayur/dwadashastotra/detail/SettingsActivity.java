package com.vtayur.dwadashastotra.detail;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vtayur.dwadashastotra.R;
import com.vtayur.dwadashastotra.data.DataProvider;
import com.vtayur.dwadashastotra.data.Language;

/**
 * Created by vtayur on 9/30/2014.
 */
public class SettingsActivity extends Activity {
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        Log.d(TAG, "settings activity being launched");

        final RadioGroup radioGrpLangSelector = (RadioGroup) findViewById(R.id.language_selector);

        radioGrpLangSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                RadioButton rb = (RadioButton) findViewById(radioGrpLangSelector.getCheckedRadioButtonId());
                editor.putString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.getLanguageEnum(rb.getText().toString()).toString());

                editor.commit();

                Log.d(TAG, "setOnCheckedChangeListener - Language settings saved - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
        String savedLocalLang = settings.getString(DataProvider.SHLOKA_DISP_LANGUAGE, "");
        if (savedLocalLang.isEmpty()) {
            Log.d(TAG, "Language settings are not set - will set to sanskrit and continue");
        }
        RadioButton rbSanskrit = (RadioButton) findViewById(R.id.language_sanskrit);
        RadioButton rbKannada = (RadioButton) findViewById(R.id.language_kannada);
        if (Language.getLanguageEnum(savedLocalLang).equals(Language.san)) {
            rbSanskrit.setChecked(true);
        } else {
            rbKannada.setChecked(true);
        }

        Log.d(TAG, "Language settings are restored - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
    }
}
