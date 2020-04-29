package com.example.scanyapp.retrofit;

import com.blankj.utilcode.util.SPStaticUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientInstance {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            String t = SPStaticUtils.getString(Static.KeyServer);
            if(!t.startsWith("http"))
                t = "https://" + t;

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(t)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


}
