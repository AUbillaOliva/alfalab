package cl.alfa.alfalab.utils;

import android.content.Context;

public class SharedPreferences {
    private android.content.SharedPreferences mSharedPreferences;

    public SharedPreferences (Context context){
        mSharedPreferences =  context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightMode (boolean state){
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("NightMode",state);
        editor.apply();
    }

    public void setFirstTime (boolean state){
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("isFirstTime", state);
        editor.apply();
    }

    public void setResponsible(String name){
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("responsibleName", name);
        editor.apply();
    }

    public void sendReports(Boolean state){
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("sendReports", state);
        editor.apply();
    }

    public boolean loadNightModeState(){ return mSharedPreferences.getBoolean("NightMode",false); }
    public boolean isFirstTime() { return mSharedPreferences.getBoolean("isFirstTime", true); }
    public String getResponsible() { return mSharedPreferences.getString("responsibleName", null); }
    public boolean sendReport(){ return mSharedPreferences.getBoolean("sendReports", false); }

}
