package org.avmrus.fopds;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import java.util.Map;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        preferences = getPreferenceManager().getSharedPreferences();
        addPreferencesFromResource(R.xml.settings);
        setListener();
        setSummaries();
    }

    private void setListener() {
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        setPreferenceSummary(preference);
        Settings.getInstance().readPreferences(preferences);
    }


    private void setSummaries() {
        Map<String, ?> allPreferences = preferences.getAll();
        for (Map.Entry<String, ?> entry : allPreferences.entrySet()) {
            onSharedPreferenceChanged(preferences, entry.getKey());
        }
    }

    private void setPreferenceSummary(Preference preference) {
        if (preference instanceof EditTextPreference) {
            preference.setSummary(((EditTextPreference) preference).getText());
        } else if (preference instanceof ListPreference) {
            preference.setSummary(((ListPreference) preference).getValue());
        }
    }
}
