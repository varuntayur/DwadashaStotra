package com.vtayur.dwadashastotra;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.vtayur.dwadashastotra.data.DataProvider;
import com.vtayur.dwadashastotra.data.DwadashaStotraMenu;
import com.vtayur.dwadashastotra.data.Language;
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
                    String isSettingAlreadySaved = settings.getString(DataProvider.LOCAL_LANGUAGE, "");
                    if (isSettingAlreadySaved.isEmpty()) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(DataProvider.LOCAL_LANGUAGE, Language.san.toString());

                        editor.commit();

                        Log.d(TAG, "Setting the default launch preference to Sanskrit at startup - " + settings.getString(DataProvider.LOCAL_LANGUAGE, ""));
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

                    String langSelected = getSharedPreferences(DataProvider.PREFS_NAME, 0).getString(DataProvider.LOCAL_LANGUAGE, "");
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