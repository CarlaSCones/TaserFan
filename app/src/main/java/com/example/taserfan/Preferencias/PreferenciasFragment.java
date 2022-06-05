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

        final EditTextPreference editTextPreference = findPreference("ip");
        editTextPreference.setSummary("Actualmente: " + GestionPreferencias.getInstance().getIp(getContext()));
        editTextPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            editTextPreference.setSummary("Actualmente: " + newValue);
            return true;
        });

        final EditTextPreference editTextPreference2 = findPreference("puerto");
        editTextPreference2.setSummary("Actualmente: " + GestionPreferencias.getInstance().getPuerto(getContext()));
        editTextPreference2.setOnPreferenceChangeListener((preference, newValue) -> {
            editTextPreference2.setSummary("Actualmente: " + newValue);
            return true;
        });

    }
}
