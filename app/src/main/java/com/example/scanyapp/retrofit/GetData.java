package com.example.scanyapp.retrofit;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetData{
    @POST("/Scanner")
    Call<List<Device>> getAllDevices(@Body String token);

    @POST("/Auth")
    Call<AuthResponse> getToken(@Body String password);
}
