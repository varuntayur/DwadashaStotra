/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vtayur.dwadashastotra.detail.stotra;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vtayur.dwadashastotra.R;
import com.vtayur.dwadashastotra.data.DataProvider;
import com.vtayur.dwadashastotra.data.Language;
import com.vtayur.dwadashastotra.data.model.Shloka;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StotraInOnePageActivity extends FragmentActivity {

    private static String TAG = "StotraInOnePageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stotra_one_page);

        Log.d(TAG, "-> Starting StotraInOnePageActivity <-");

        Typeface typeface = getTypeface();

        final LinearLayout rootLayout = (LinearLayout) findViewById(R.id.rootLayout);

        Integer menuPosition = getIntent().getIntExtra("menuPosition", 0);
        rootLayout.setBackgroundResource(DataProvider.getBackgroundColor(menuPosition - 1));

        TextView tvTitle = (TextView) findViewById(R.id.sectiontitle);
        tvTitle.setText(getIntent().getStringExtra("sectionName"));

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        List<Shloka> engShlokas = (List<Shloka>) getIntent().getSerializableExtra("shlokaList");
        List<Shloka> localLangShlokas = (List<Shloka>) getIntent().getSerializableExtra("shlokaListLocalLang");

        Log.d(TAG, "StotraInOnePageActivity needs to render " + localLangShlokas.size() + " shlokas");
        Log.d(TAG, "StotraInOnePageActivity needs to render english " + engShlokas.size() + " shlokas");

        List<Pair<Shloka, Shloka>> lstPairShlokas = getListPairedShlokas(engShlokas, localLangShlokas);

        for (Pair<Shloka, Shloka> shlokaPair : lstPairShlokas) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            TextView localLang = new TextView(this);
            localLang.setTypeface(typeface);
            localLang.setText(shlokaPair.second.getText());

            TextView engLang = new TextView(this);
            engLang.setText(shlokaPair.first.getText());

            ll.addView(localLang);
            ll.addView(engLang);

            ll.setPadding(0, 5, 0, 50);
            rootLayout.addView(ll);
        }

        Log.d(TAG, "* StotraInOnePageActivity created *");
    }

    private List<Pair<Shloka, Shloka>> getListPairedShlokas(List<Shloka> engShlokas, List<Shloka> localLangShlokas) {
        List<Pair<Shloka, Shloka>> lstPairShlokas = new ArrayList<Pair<Shloka, Shloka>>();

        Iterator<Shloka> iterLocalLang = localLangShlokas.iterator();
        for (Shloka shloka : engShlokas) {
            if (iterLocalLang.hasNext()) {
                Pair<Shloka, Shloka> pair = new Pair<Shloka, Shloka>(shloka, iterLocalLang.next());
                lstPairShlokas.add(pair);
            } else {
                Pair<Shloka, Shloka> pair = new Pair<Shloka, Shloka>(shloka, new Shloka());
                lstPairShlokas.add(pair);
            }
        }

        if (engShlokas.size() < localLangShlokas.size()) {
            Log.w(TAG, "getListPairedShlokas found a mismatch in eng vs. local lang.. " + engShlokas.size() + " vs. " + localLangShlokas.size() + " shlokas");
            while (iterLocalLang.hasNext()) {
                Pair<Shloka, Shloka> pair = new Pair<Shloka, Shloka>(new Shloka(), iterLocalLang.next());
                lstPairShlokas.add(pair);
            }

        }

        return lstPairShlokas;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private Typeface getTypeface() {
        String langPrefs = getSelectedLanguage();

        Log.d(TAG, "Trying to launch activity in selected language :" + langPrefs);

        Language lang = Language.getLanguageEnum(langPrefs);

        Log.d(TAG, "Will get assets for activity in language :" + lang.toString());

        return lang.getTypeface(getAssets());
    }

    private String getSelectedLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataProvider.PREFS_NAME, 0);
        return sharedPreferences.getString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.san.toString());
    }
}
