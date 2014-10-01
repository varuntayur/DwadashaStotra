package com.vtayur.dwadashastotra.data;

import android.content.res.AssetManager;
import android.util.Log;

import com.vtayur.dwadashastotra.R;
import com.vtayur.dwadashastotra.data.model.DwadashaStotra;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by vtayur on 8/19/2014.
 */
public class DataProvider {

    private static final String TAG = "DataProvider";

    public static final String PREFS_NAME = "DwadashaStotra";
    public static final String SHLOKA_DISP_LANGUAGE = "localLanguage";
    public static final String LEARNING_MODE = "learningMode";

    private static Map<String, DwadashaStotra> lang2DwadashaStotra = new ConcurrentHashMap<String, DwadashaStotra>();

    private static List<Integer> mBackgroundColors = new ArrayList<Integer>() {
        {
            add(R.color.orange);
            add(R.color.green);
            add(R.color.blue);
            add(R.color.yellow);
            add(R.color.grey);
            add(R.color.lblue);
            add(R.color.slateblue);
            add(R.color.cyan);
            add(R.color.silver);
        }
    };

    private final static CharSequence[] languages = {"Sanskrit", "Kannada"};

    public static List<Integer> getBackgroundColorList() {
        return Collections.unmodifiableList(mBackgroundColors);
    }

    public static List<String> getMenuNames() {

        String anyResource = lang2DwadashaStotra.keySet().iterator().next();

        return new ArrayList<String>(DataProvider.getDwadashaStotra(Language.getLanguageEnum(anyResource)).getSectionNames());
    }

    public static int getBackgroundColor(int location) {
        return mBackgroundColors.get(location % mBackgroundColors.size());
    }

    public static void init(AssetManager am) {
        Serializer serializer;
        InputStream inputStream;
        try {
            inputStream = am.open("db/dwadashastotra-eng.xml");
            serializer = new Persister();
            DwadashaStotra vayuSthuthi = serializer.read(DwadashaStotra.class, inputStream);
            lang2DwadashaStotra.put(vayuSthuthi.getLang(), vayuSthuthi);
            Log.d(TAG, "* Finished de-serializing the file - dwadashastotra-eng.xml *");
//            System.out.println(vayuSthuthi);

            inputStream = am.open("db/dwadashastotra-kan.xml");
            serializer = new Persister();
            vayuSthuthi = serializer.read(DwadashaStotra.class, inputStream);
            lang2DwadashaStotra.put(vayuSthuthi.getLang(), vayuSthuthi);
            Log.d(TAG, "* Finished de-serializing the file - dwadashastotra-kan.xml *");
//            System.out.println(vayuSthuthi);

            inputStream = am.open("db/dwadashastotra-san.xml");
            serializer = new Persister();
            vayuSthuthi = serializer.read(DwadashaStotra.class, inputStream);
            lang2DwadashaStotra.put(vayuSthuthi.getLang(), vayuSthuthi);
            Log.d(TAG, "* Finished de-serializing the file - dwadashastotra-san.xml *");
//            System.out.println(vayuSthuthi);

            System.out.println(lang2DwadashaStotra.keySet());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "* IOException de-serializing the file *" + e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "* Exception de-serializing the file *" + e);
        }
    }

    public static DwadashaStotra getDwadashaStotra(Language lang) {
        return lang2DwadashaStotra.get(lang.toString());
    }


}
