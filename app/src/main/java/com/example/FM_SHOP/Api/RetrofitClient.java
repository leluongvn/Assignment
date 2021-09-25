package com.example.FM_SHOP.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit instance;

    public static Retrofit getInstance(){
        if (instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.3:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}
