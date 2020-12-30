package cl.alfa.alfalab.utils;

import android.content.Context;

import cl.alfa.alfalab.models.AuthUser;
import cl.alfa.alfalab.models.User;

public class SharedPreferences {
    private final android.content.SharedPreferences mSharedPreferences;

    public SharedPreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightModeState(boolean state) {
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }

    public void setFirstTime(boolean state) {
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean("isFirstTime", state);
        editor.apply();
    }

    public void setResponsible(AuthUser user) {
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("firstname", user.getFirstname());
        editor.putString("lastname", user.getLastname());
        editor.putString("email", user.getEmail());
        editor.putString("profileImage", user.getProfileImage());
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

    public void signOut() {
        final android.content.SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove("token");
        editor.remove("firstname");
        editor.remove("lastname");
        editor.apply();
    }

    public boolean loadNightModeState() {
        return mSharedPreferences.getBoolean("NightMode", false);
    }

    public boolean isFirstTime() {
        return mSharedPreferences.getBoolean("isFirstTime", true);
    }

    public User getResponsible() {
        return new User(mSharedPreferences.getString("firstname", null), mSharedPreferences.getString("lastname", null), mSharedPreferences.getString("email", null), null, mSharedPreferences.getString("profileImage", null));
    }

    public boolean sendReport() {
        return mSharedPreferences.getBoolean("sendReports", false);
    }

    public String getToken() {
        return mSharedPreferences.getString("token", null);
    }

}
