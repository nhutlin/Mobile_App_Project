package com.example.uit_project.api;
import com.example.uit_project.model.User;
import com.example.uit_project.model.datapoint.RequestBody;
import com.example.uit_project.model.light.LightAsset;
import com.example.uit_project.model.weather.WeatherAssetResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {
    Gson gson = new GsonBuilder().create();
    APIService apiService = new Retrofit.Builder()
            .baseUrl("https://uiot.ixxc.dev/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    @GET("api/master/asset/{assetID}")
    Call<WeatherAssetResponse> getWeatherAsset(@Path("assetID") String assetID,
                                               @Header("Authorization") String auth);

    @GET("api/master/asset/{assetID}")
    Call<LightAsset> getLightAsset(@Path("assetID") String assetID,
                                   @Header("Authorization") String auth);

    @POST("/api/master/asset/datapoint/{assetId}/attribute/{attributeName}")
    Call<JsonArray> getDatapoint(@Header("Authorization") String auth,
                                               @Path("assetId") String assetId ,
                                               @Path("attributeName") String attributeName,
                                               @Body RequestBody Body);
    @GET("api/master/user/user")
    Call<User> getUser(@Header("Authorization") String auth);
}
