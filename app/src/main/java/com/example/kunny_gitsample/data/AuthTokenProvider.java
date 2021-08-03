package com.example.kunny_gitsample.data;

import android.content.Context;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

public class AuthTokenProvider {
    private static final String KEY_AUTH_TOKEN = "auth_token";

    private Context context;

    public AuthTokenProvider(@NonNull Context context) {
        this.context = context;
    }

    public void updateToken(@NonNull  String token){
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_AUTH_TOKEN, token)
                .apply();
    }

    public String getToken(){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_AUTH_TOKEN, null);
    }

}
