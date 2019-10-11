package com.vtayur.dwadashastotra.detail.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vtayur.dwadashastotra.R;
import com.vtayur.dwadashastotra.data.DataProvider;
import com.vtayur.dwadashastotra.data.Language;
import com.vtayur.dwadashastotra.data.YesNo;
import com.vtayur.dwadashastotra.detail.common.CustomAdapter;
import com.vtayur.dwadashastotra.detail.settings.SettingsActivity;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static String TAG = "HomeActivity";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

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

        return super.onOptionsItemSelected(item);
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

//                    TextView viewById = (TextView)findViewById(R.id.txt_title);
//                    viewById.setText(R.string.app_name);

                    final List<String> sectionNames = DataProvider.getMenuNames();
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

                    // set a StaggeredGridLayoutManager with 3 number of columns and vertical orientation
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);

                    GridLayoutManager layoutManager = new GridLayoutManager(activity, 2);

                    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            if (position > 0)
                                return 1;
                            else
                                return 2;
                        }
                    });
                    recyclerView.setLayoutManager(layoutManager);

                    CustomAdapter customAdapter = new CustomAdapter(activity, sectionNames);
                    recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

                    SharedPreferences settings = getSharedPreferences(DataProvider.PREFS_NAME, 0);
                    String isLocalLangAlreadySaved = settings.getString(DataProvider.SHLOKA_DISP_LANGUAGE, "");
                    String isLearningModeSaved = settings.getString(DataProvider.LEARNING_MODE, "");
                    if (isLocalLangAlreadySaved.isEmpty()) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(DataProvider.SHLOKA_DISP_LANGUAGE, Language.san.toString());

                        editor.commit();

                        Log.d(TAG, "Setting the default launch language preference to Sanskrit at startup - " + settings.getString(DataProvider.SHLOKA_DISP_LANGUAGE, ""));
                    }

                    if (isLearningModeSaved.isEmpty()) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(DataProvider.LEARNING_MODE, YesNo.yes.toString());

                        editor.commit();

                        Log.d(TAG, "Setting the default learn mode preference to 'Yes' - " + settings.getString(DataProvider.LEARNING_MODE, ""));
                    }
                    progressDialog.dismiss();
                }
            });
            Log.d(TAG, "Finished launching main-menu");

        }

        @Override
        protected Long doInBackground(AssetManager... assetManagers) {

            DataProvider.init(getAssets());
            Log.d(TAG, "Finished background task execution.");
            return 1l;
        }
    }

    public void launchMadhvanama(View v) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("market://details?id=com.vtayur.madhvanama"));
        startActivity(intent);
    }
}