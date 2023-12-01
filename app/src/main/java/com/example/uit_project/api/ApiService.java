package com.example.uit_project.api;
import com.example.uit_project.model.Asset;
import com.example.uit_project.model.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    Gson gson = new GsonBuilder().create();
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://uiot.ixxc.dev/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @FormUrlEncoded
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<ApiResponse> getToken(@Field("client_id") String client_id,
                               @Field("username") String username,
                               @Field("password") String password,
                               @Field("grant_type") String grant_type);

    @GET("api/master/asset/{assetID}")
    Call<Asset> getAsset(@Path("assetID") String assetID, @Header("Authorization") String auth);
}
