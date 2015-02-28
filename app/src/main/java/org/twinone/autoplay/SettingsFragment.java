package org.twinone.autoplay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment {
    public static final String FILENAME = "com.twinone.autoplay.settings";

    Preference mMoreFromDeveloper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager pm = getPreferenceManager();
        pm.setSharedPreferencesName(FILENAME);
        pm.setSharedPreferencesMode(Context.MODE_PRIVATE);

        addPreferencesFromResource(R.xml.prefs);
        mMoreFromDeveloper = findPreference(getString(R.string.pref_key_more_from_dev));
        mMoreFromDeveloper.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
             showMarket(getActivity());
                return true;
            }
        });

    }
    private static void showMarket(Context c) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://search?q=pub:Twinone"));
        if (c.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            c.startActivity(intent);
        }
    }

}
