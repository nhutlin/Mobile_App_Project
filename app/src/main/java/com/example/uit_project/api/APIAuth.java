package com.example.uit_project.api;

import com.example.uit_project.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIAuth {
    @FormUrlEncoded
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<ApiResponse> getToken(@Field("client_id") String client_id,
                               @Field("username") String username,
                               @Field("password") String password,
                               @Field("grant_type") String grant_type);

}
