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

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.vtayur.dwadashastotra.R;
import com.vtayur.dwadashastotra.data.DataProvider;
import com.vtayur.dwadashastotra.data.Language;
import com.vtayur.dwadashastotra.data.model.Shloka;
import com.vtayur.dwadashastotra.detail.common.BundleArgs;
import com.vtayur.dwadashastotra.detail.common.ZoomOutPageTransformer;

import java.io.Serializable;
import java.util.List;

public class StotraSlideBrowseActivity extends FragmentActivity {



    private static String TAG = "ShlokaSlideActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shloka_slide);

        Log.d(TAG, "-> Starting ScreenSlideActivity <-");

        Typeface langTypeface = getTypeface();

        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        Integer menuPosition = getIntent().getIntExtra(BundleArgs.PAGE_NUMBER, 0);
        mPager.setBackgroundResource(DataProvider.getBackgroundColor((menuPosition - 1) < 0 ? 1 : menuPosition - 1));

        String mSectionName = getIntent().getStringExtra(BundleArgs.SECTION_NAME);
        List<Shloka> engShlokas = (List<Shloka>) getIntent().getSerializableExtra(BundleArgs.ENG_SHLOKA_LIST);

        List<Shloka> localLangShlokas = (List<Shloka>) getIntent().getSerializableExtra(BundleArgs.LOCAL_LANG_SHLOKA_LIST);

        PagerAdapter mPagerAdapter = new ShlokaSlidePagerAdapter(mSectionName, engShlokas, localLangShlokas, getFragmentManager(), langTypeface);

        mPager.setAdapter(mPagerAdapter);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });
        Log.d(TAG, "* ScreenSlideActivity created *");
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

    /**
     * A simple pager adapter that represents 5 {@link StotraPageFragment} objects, in
     * sequence.
     */
    private class ShlokaSlidePagerAdapter extends FragmentStatePagerAdapter {


        private final Typeface tf;
        private final String sectionName;
        private List<Shloka> shlokas;
        private List<Shloka> localLangShlokas;

        public ShlokaSlidePagerAdapter(String sectionName, List<Shloka> shlokas, List<Shloka> localizedShlokas, FragmentManager fm, Typeface tf) {
            super(fm);
            this.tf = tf;
            this.shlokas = shlokas;
            this.sectionName = sectionName;
            this.localLangShlokas = localizedShlokas;
        }

        @Override
        public Fragment getItem(int position) {
            StotraPageFragment stotraPageFragment = new StotraPageFragment();
            Bundle bundleArgs = new Bundle();
            bundleArgs.putString(BundleArgs.SECTION_NAME, sectionName);
            bundleArgs.putSerializable(BundleArgs.ENG_SHLOKA_LIST, (Serializable) shlokas);
            bundleArgs.putSerializable(BundleArgs.LOCAL_LANG_SHLOKA_LIST, (Serializable) localLangShlokas);
            bundleArgs.putInt(BundleArgs.PAGE_NUMBER, position);
            stotraPageFragment.setArguments(bundleArgs);
            return stotraPageFragment;
        }

        @Override
        public int getCount() {
            return shlokas.size();
        }
    }
}
