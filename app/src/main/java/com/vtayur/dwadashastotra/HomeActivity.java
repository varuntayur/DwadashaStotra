package com.vtayur.dwadashastotra;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.vtayur.dwadashastotra.data.DataProvider;
import com.vtayur.dwadashastotra.data.DwadashaStotraMenu;
import com.vtayur.dwadashastotra.data.Language;
import com.vtayur.dwadashastotra.data.YesNo;
import com.vtayur.dwadashastotra.detail.StaggeredGridAdapter;

import java.util.List;

public class HomeActivity extends Activity {

    private static String TAG = "HomeActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sgv);

        progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.loading_please_wait), true);

        new DataProviderTask(this).execute(getAssets());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

                TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
                int defaultColor = textView.getTextColors().getDefaultColor();
                textView.setTextColor(defaultColor);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.ic_launcher);
                builder.setTitle(R.string.app_name);
                builder.setView(messageView);
                builder.create();
                builder.show();

                return true;
            case R.id.settings:

                LayoutInflater inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.settings, null);
                AlertDialog.Builder builderSettings = new AlertDialog.Builder(this);
                builderSettings.setView(dialoglayout);
                final RadioGroup radioGrpLangSelector = (RadioGroup) dialoglayout.findViewById
                        (R.id.language_selector);

                radioGrpLangSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        Log.d(TAG, "setOnCheckedChangeListener-radioGrpLangSelector-" + checkedId);

                        SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        RadioButton rb = (RadioButton) dialoglayout.findViewById(radioGrpLangSelector.getCheckedRadioButtonId());
                        editor.putString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.getLanguageEnum(rb.getText().toString()).toString());

                        editor.commit();

                        Log.d(TAG, "Language settings saved - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
                    }
                });

                final RadioGroup radioGrpAutoScroll = (RadioGroup) dialoglayout.findViewById
                        (R.id.auto_scroll_selector);

                radioGrpAutoScroll.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        Log.d(TAG, "setOnCheckedChangeListener - radioGrpAutoScroll-" + checkedId);

                        SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        RadioButton rb = (RadioButton) dialoglayout.findViewById(radioGrpAutoScroll.getCheckedRadioButtonId());
                        editor.putString(DataProvider.AUTO_SCROLL_SHLOKA, YesNo.getYesNoEnum(rb.getText().toString()).toString());

                        editor.commit();

                        Log.d(TAG, "Auto Scroll settings saved - " + getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.AUTO_SCROLL_SHLOKA, ""));
                    }
                });


                builderSettings.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d(TAG, "Close button clicked " + which);
                    }
                });

                builderSettings.show();
                break;

//            case R.id.languageselector:
//                AlertDialog.Builder builderLangSelector = new AlertDialog.Builder(this);
//                builderLangSelector.setTitle("Pick a Language");
//
//                SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
//                String savedLocalLanguage = settings.getString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.san.toString());
//                Log.d(TAG, "Language restored/saved - " + savedLocalLanguage);
//                builderLangSelector.setSingleChoiceItems(DataProvider.getLanguages(), Language.getLanguageEnum(savedLocalLanguage).ordinal(), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.d(TAG, "Language selected - " + which);
//
//                        SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.getLanguageEnum(which).toString());
//
//                        editor.commit();
//
//                        Log.d(TAG, "Language settings saved - " + getSharedPreferences(DataProvider.PREFS_NAME, 0));
//
//                    }
//                });
//                builderLangSelector.create();
//                builderLangSelector.show();
//                break;
//            case R.id.auto_scroll_group:
//                Log.d(TAG, "Auto Scroll setting displayed - ");
//                break;
//            case R.id.auto_scroll_yes:
//                if (item.isChecked()) item.setChecked(false);
//                else item.setChecked(true);
//                Log.d(TAG, "Auto Scroll setting Scroll Yes - " + item.isChecked());
//                break;
//            case R.id.auto_scroll_no:
//                if (item.isChecked()) item.setChecked(false);
//                else item.setChecked(true);
//                Log.d(TAG, "Auto Scroll setting Scroll No - " + item.isChecked());
//                break;
//
//            case R.id.language_kannada:
//                if (item.isChecked()) item.setChecked(false);
//                else item.setChecked(true);
//                Log.d(TAG, "Language Kannada selected - " + item.isChecked());
//                break;
//            case R.id.language_sanskrit:
//                if (item.isChecked()) item.setChecked(false);
//                else item.setChecked(true);
//                Log.d(TAG, "Language Sanskrit selected  - " + item.isChecked());
//                break;
            default:
                break;
        }

        return super.

                onOptionsItemSelected(item);
    }


    private class DataProviderTask extends AsyncTask<AssetManager, Void, Long> {

        Activity currentActivity;

        public DataProviderTask(Activity activity) {
            this.currentActivity = activity;
        }

        protected void onPostExecute(Long result) {
            final LayoutInflater inflater = currentActivity.getLayoutInflater();
            final Activity activity = this.currentActivity;
            runOnUiThread(new Runnable() {
                public void run() {

                    StaggeredGridView listView = (StaggeredGridView) findViewById(R.id.grid_view);

                    View header = inflater.inflate(R.layout.list_item_header_footer, null);
                    TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
                    txtHeaderTitle.setText(getResources().getString(R.string.app_name));

                    listView.addHeaderView(header);
                    header.setClickable(false);

                    StaggeredGridAdapter mAdapter = new StaggeredGridAdapter(activity, R.id.txt_line1);

                    final List<String> sectionNames = DataProvider.getMenuNames();

                    for (String data : sectionNames) {
                        mAdapter.add(data);
                    }
                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(getOnMenuClickListener(activity));

                    SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
                    String isLocalLangAlreadySaved = settings.getString(DataProvider.SHLOKA_DISP_LANGUAGE, "");
                    String isAutoScrollAlreadySaved = settings.getString(DataProvider.AUTO_SCROLL_SHLOKA, "");
                    if (isLocalLangAlreadySaved.isEmpty()) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.san.toString());

                        editor.commit();

                        Log.d(TAG, "Setting the default launch language preference to Sanskrit at startup - " + settings.getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
                    }

                    if (isAutoScrollAlreadySaved.isEmpty()) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(DataProvider.AUTO_SCROLL_SHLOKA, YesNo.no.toString());

                        editor.commit();

                        Log.d(TAG, "Setting the default launch auto scroll preference to 'no' - " + settings.getString(DataProvider.AUTO_SCROLL_SHLOKA, ""));
                    }
                    progressDialog.dismiss();
                }
            });
            Log.d(TAG, "Finished launching main-menu");

        }

        private AdapterView.OnItemClickListener getOnMenuClickListener(final Activity activity) {
            return new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = (String) parent.getAdapter().getItem(position);

                    String langSelected = getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.SHLOKA_DISP_LANGUAGE, "");
                    DwadashaStotraMenu.getEnum(item).execute(activity, item, position, Language.getLanguageEnum(langSelected));

                }
            };
        }

        @Override
        protected Long doInBackground(AssetManager... assetManagers) {

            DataProvider.init(getAssets());
            Log.d(TAG, "Finished background task execution.");
            return 1l;
        }
    }
}