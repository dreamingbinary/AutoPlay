package org.twinone.autoplay;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends PreferenceFragment {
    public static final String FILENAME = "com.twinone.autoplay.settings";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager pm = getPreferenceManager();
        pm.setSharedPreferencesName(FILENAME);
        pm.setSharedPreferencesMode(Context.MODE_PRIVATE);

        addPreferencesFromResource(R.xml.prefs);
    }


}
