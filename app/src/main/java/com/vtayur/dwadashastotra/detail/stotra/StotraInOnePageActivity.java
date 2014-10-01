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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vtayur.dwadashastotra.R;
import com.vtayur.dwadashastotra.data.DataProvider;
import com.vtayur.dwadashastotra.data.Language;
import com.vtayur.dwadashastotra.data.model.Shloka;

import java.util.List;

/**
 * Demonstrates a "screen-slide" animation using a {@link android.support.v4.view.ViewPager}. Because {@link android.support.v4.view.ViewPager}
 * automatically plays such an animation when calling {@link android.support.v4.view.ViewPager#setCurrentItem(int)}, there
 * isn't any animation-specific code in this sample.
 * <p/>
 * <p>This sample shows a "next" button that advances the user to the next step in a wizard,
 * animating the current screen out (to the left) and the next screen in (from the right). The
 * reverse animation is played when the user presses the "previous" button.</p>
 *
 * @see com.vtayur.dwadashastotra.detail.stotra.StotraPageFragment
 */
public class StotraInOnePageActivity extends FragmentActivity {

    private static String TAG = "StotraInOnePageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stotra_one_page);

        Log.d(TAG, "-> Starting StotraInOnePageActivity <-");

        Typeface langTypeface = getTypeface();

        final LinearLayout lm = (LinearLayout) findViewById(R.id.rootLayout);

        // create the layout params that will be used to define how your
        // button will be displayed
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Create LinearLayout
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        // Create TextView
        TextView product = new TextView(this);
        product.setText(" Product"+0+"    ");
        ll.addView(product);

        lm.addView(ll);

//        Integer menuPosition = getIntent().getIntExtra("menuPosition", 0);
//        mPager.setBackgroundResource(DataProvider.getBackgroundColor(menuPosition - 1));
//
//        String mSectionName = getIntent().getStringExtra("sectionName");
//        List<Shloka> engShlokas = (List<Shloka>) getIntent().getSerializableExtra("shlokaList");
//
//        List<Shloka> localLangShlokas = (List<Shloka>) getIntent().getSerializableExtra("shlokaListLocalLang");
//
//        PagerAdapter mPagerAdapter = new ShlokaSlidePagerAdapter(mSectionName, engShlokas, localLangShlokas, getFragmentManager(), langTypeface);
//
//        mPager.setAdapter(mPagerAdapter);
//
//        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                invalidateOptionsMenu();
//            }
//        });
        Log.d(TAG, "* StotraInOnePageActivity created *");
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
     * A simple pager adapter that represents 5 {@link com.vtayur.dwadashastotra.detail.stotra.StotraPageFragment} objects, in
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
            return new StotraPageFragment(sectionName, shlokas, localLangShlokas, position, tf);
        }

        @Override
        public int getCount() {
            return shlokas.size();
        }
    }
}
