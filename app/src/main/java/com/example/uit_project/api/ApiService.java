package com.example.uit_project.api;
import com.example.uit_project.model.ApiRespone;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<ApiRespone> getToken(@Field("client_id") String client_id,
                              @Field("username") String username,
                              @Field("password") String password,
                              @Field("grant_type") String grant_type);


}
