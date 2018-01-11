package com.agh.wfiis.piase.inz.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.agh.wfiis.piase.inz.R;

import java.util.Map;

/**
 * Created by piase on 2018-01-10.
 */

public class UserPreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private UserPreferencesActivity.UserPreferencesFragment userPreferencesFragment;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPreferencesFragment = new UserPreferencesFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, userPreferencesFragment).commit();
        getFragmentManager().executePendingTransactions();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        //updatePreferences();
    }

    public void updatePreferences() {
        Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
        for (String key : sharedPreferencesAll.keySet()) {
            Object type = sharedPreferencesAll.get(key);
            if (type instanceof String) {
                String keyValue = sharedPreferences.getString(key, "");
                Preference preference =  userPreferencesFragment.findPreference(keyValue);
                if (preference != null) {
                    Log.i("key:" + key, "val:" + keyValue);
                    preference.setSummary(keyValue);
                }
            }

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String keyValue = sharedPreferences.getString(key, "");
        Preference preference = userPreferencesFragment.findPreference(keyValue);
        Log.i("key:" + key, "val:" + keyValue);
        if (preference != null) {
           // preference.setSummary(keyValue);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    public static class UserPreferencesFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.server_preferences);
        }

    }

}
