package cl.alfa.alfalab.utils;

import android.content.Context;

import cl.alfa.alfalab.models.User;

public class SharedPreferences {
    private android.content.SharedPreferences mSharedPreferences;

    public SharedPreferences (Context context) {
        mSharedPreferences =  context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightModeState(boolean state) {
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("NightMode",state);
        editor.apply();
    }

    public void setFirstTime (boolean state) {
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("isFirstTime", state);
        editor.apply();
    }

    public void setResponsible(User user) {
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("firstname", user.getFirstname());
        editor.putString("lastname", user.getLastname());
        editor.apply();
    }

    public void sendReports(Boolean state) {
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("sendReports", state);
        editor.apply();
    }

    public void setToken(String token) {
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public boolean loadNightModeState() { return mSharedPreferences.getBoolean("NightMode",false); }
    public boolean isFirstTime() { return mSharedPreferences.getBoolean("isFirstTime", true); }
    public User getResponsible() { return new User(mSharedPreferences.getString("firstname", null), mSharedPreferences.getString("lastname", null)); }
    public boolean sendReport() { return mSharedPreferences.getBoolean("sendReports", false); }
    public String getToken() { return mSharedPreferences.getString("token", null); }

}
