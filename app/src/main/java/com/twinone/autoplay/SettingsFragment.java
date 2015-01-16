package com.twinone.autoplay;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment {
	public static final String FILENAME = "com.twinone.autoplay.settings";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager pm = getPreferenceManager();
		pm.setSharedPreferencesName(FILENAME);
		pm.setSharedPreferencesMode(Context.MODE_PRIVATE);

		addPreferencesFromResource(R.xml.prefs);

		ActionBar ab = ((MainActivity) getActivity()).getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

	}

}
