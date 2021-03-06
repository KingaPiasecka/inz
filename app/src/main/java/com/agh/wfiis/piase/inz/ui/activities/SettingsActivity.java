package com.agh.wfiis.piase.inz.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.agh.wfiis.piase.inz.R;

import java.util.Map;

/**
 * Created by piase on 2018-01-07.
 */

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private PreferencesFragment preferencesFragment;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Preferences");
        preferencesFragment = new PreferencesFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, preferencesFragment).commit();
        getFragmentManager().executePendingTransactions();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
       // updatePreferences();
    }

    public void updatePreferences() {
        Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
        for (String key : sharedPreferencesAll.keySet()) {
            Object type = sharedPreferencesAll.get(key);
            if (type instanceof String) {
                String keyValue = sharedPreferences.getString(key, "");
                Preference preference = preferencesFragment.findPreference(keyValue);
                if (preference != null) {
                    Log.i("key:" + key, "val:" + keyValue);
                    //preference.setSummary(keyValue);
                }
            }

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String keyValue = sharedPreferences.getString(key, "");
        Preference preference = preferencesFragment.findPreference(keyValue);
        Log.i("key:" + key, "val:" + keyValue);
        if (preference != null) {
            //preference.setSummary(keyValue);
        }
    }


    public static class PreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

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
}
