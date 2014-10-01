package com.vtayur.dwadashastotra.data;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.vtayur.dwadashastotra.data.model.DwadashaStotra;
import com.vtayur.dwadashastotra.data.model.Section;
import com.vtayur.dwadashastotra.detail.stotra.StotraInOnePageActivity;
import com.vtayur.dwadashastotra.detail.stotra.StotraSlideBrowseActivity;

import java.io.Serializable;

/**
 * Created by varuntayur on 6/21/2014.
 */
public enum DwadashaStotraMenu {

    DEFAULT("Default") {
        @Override
        public void execute(Activity activity, String item, int position, Language language) {
            Intent intent = null;
            SharedPreferences settings = activity.getSharedPreferences(DataProvider.PREFS_NAME, 0);

            String learningMode = settings.getString(DataProvider.LEARNING_MODE, "");

            if (learningMode.equalsIgnoreCase(YesNo.yes.toString()))
                intent = new Intent(activity, StotraSlideBrowseActivity.class);
            else
                intent = new Intent(activity, StotraInOnePageActivity.class);

            intent.putExtra("sectionName", item);
            intent.putExtra("menuPosition", position);

            Section section = DataProvider.getDwadashaStotra(Language.eng).getSection(item);

            if (section == null) return;

            intent.putExtra("shlokaList", (Serializable) section.getShlokaList());

            DwadashaStotra sanVayuStuthi = DataProvider.getDwadashaStotra(language);
            section = sanVayuStuthi.getSection(item);
            Log.d(TAG, "item section ->" + item + " " + section);
            intent.putExtra("shlokaListLocalLang", (Serializable) section.getShlokaList());

            activity.startActivity(intent);
        }
    };

    private static final String TAG = "DwadashaStotraMenu";
    private String menuDisplayName;

    DwadashaStotraMenu(String menu) {
        this.menuDisplayName = menu;
    }

    public static DwadashaStotraMenu getEnum(String item) {
        for (DwadashaStotraMenu v : values())
            if (v.toString().equalsIgnoreCase(item)) return v;
        return DwadashaStotraMenu.DEFAULT;
    }

    @Override
    public String toString() {
        return menuDisplayName;
    }

    public abstract void execute(Activity activity, String item, int position, Language language);
}
