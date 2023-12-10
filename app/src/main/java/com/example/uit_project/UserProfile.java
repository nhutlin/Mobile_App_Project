package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uit_project.api.ApiService;
import com.example.uit_project.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ApiService.apiService.getUser().enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        User user = response.body();

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                }
        );
    }
}