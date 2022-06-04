package com.example.taserfan.Preferencias;


import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.taserfan.R;

public class PreferenciasFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.preferencias, rootKey);

        ListPreference themePreference = getPreferenceManager().findPreference(getString(R.string.settings_theme_key));
        if (themePreference.getValue() == null) {
            themePreference.setValue(ThemeSetup.Mode.DEFAULT.name());
        }
        themePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            ThemeSetup.applyTheme(ThemeSetup.Mode.valueOf((String) newValue));
            return true;
        });

        final EditTextPreference ip = getPreferenceManager().findPreference("ip");
        ip.setSummary("IP actual: " + GestionPreferencias.getInstance().getIp(getContext()));
        ip.setOnPreferenceChangeListener((preference, newValue) -> {
            ip.setSummary("IP actual: " + newValue);
            return true;
        });

        // puerto
        final EditTextPreference url = findPreference("puerto");
        url.setSummary("Puerto actual: " + GestionPreferencias.getInstance().getPuerto(getContext()));
        url.setOnPreferenceChangeListener((preference, newValue) -> {
            url.setSummary("Puerto actual: " + newValue);
            return true;
        });
    }
}
