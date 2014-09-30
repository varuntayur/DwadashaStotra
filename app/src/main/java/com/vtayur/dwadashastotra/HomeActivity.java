package com.vtayur.dwadashastotra;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.vtayur.dwadashastotra.data.DataProvider;
import com.vtayur.dwadashastotra.data.DwadashaStotraMenu;
import com.vtayur.dwadashastotra.data.Language;
import com.vtayur.dwadashastotra.data.YesNo;
import com.vtayur.dwadashastotra.detail.SettingsActivity;
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
                builder.setIcon(R.drawable.dwadashastotra);
                builder.setTitle(R.string.app_name);
                builder.setView(messageView);
                builder.create();
                builder.show();

                return true;
            case R.id.settings:

                Intent intent = new Intent(this, SettingsActivity.class);
                Log.d(TAG, "Launching Settings Activity");
                startActivity(intent);

                break;
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