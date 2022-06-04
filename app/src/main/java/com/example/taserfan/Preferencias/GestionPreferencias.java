package com.example.taserfan.Preferencias;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.taserfan.API.API;
import com.example.taserfan.R;

public class GestionPreferencias {

    private SharedPreferences pref;
    private static GestionPreferencias gestionPreferencias;

    private GestionPreferencias(){

    }

    public static GestionPreferencias getInstance(){
        if(gestionPreferencias==null)
            gestionPreferencias = new GestionPreferencias();
        return gestionPreferencias;
    }

    private void inicializa(Context context) {
        if (pref == null)
            pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getTheme(Context context){
        inicializa(context);
        return pref.getString(context.getString(R.string.settings_theme_key),ThemeSetup.Mode.DEFAULT.name());
    }

    public String getIp(Context context){
        inicializa(context);
        return pref.getString("ip", API.Routes.IP);
    }

    public String getPuerto(Context context){
        inicializa(context);
        return pref.getString("puerto", API.Routes.PUERTO);
    }
}