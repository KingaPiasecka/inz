package com.agh.wfiis.piase.inz.restapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.agh.wfiis.piase.inz.restapi.auth.AuthInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by piase on 2017-12-18.
 */

public class APIClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        String ip = preferences.getString("ip", "");
        StringBuilder url = new StringBuilder();
        url.append("http://").append(ip);

        AuthInterceptor authInterceptor = new AuthInterceptor(username, password);
        Log.i("u" + username + "" + password, "" + url.toString());
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd' 'HH:mm:ss")
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(url.toString())
                .client(new OkHttpClient.Builder().addInterceptor(authInterceptor).build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

}
