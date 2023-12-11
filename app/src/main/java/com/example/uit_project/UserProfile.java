package com.example.uit_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.uit_project.api.ApiService;
import com.example.uit_project.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {
    private TextView realm;
    private TextView userId;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView createOn;
    private TextView enabled;
    private TextView userName;
    private TextView serviceAccount;

    private Button logOut;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        realm = findViewById(R.id.realm_value);
        userId = findViewById(R.id.user_id_value);
        firstName = findViewById(R.id.first_name_value);
        lastName = findViewById(R.id.last_name_value);
        email = findViewById(R.id.email_value);
        enabled = findViewById(R.id.enabled_value);
        userName = findViewById(R.id.username_value);
        serviceAccount = findViewById(R.id.service_value);
        createOn = findViewById(R.id.create_on_value);
        back = findViewById(R.id.btn_back);
        logOut = findViewById(R.id.btn_logout);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserProfile.this, Login.class);
                startActivity(intent);
            }
        });

        ApiService.apiService.getUser("Bearer " + GlobalVar.tokenProfile).enqueue(
                new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        Log.d("API CALL", response.code() + "");

                        User userAsset = response.body();
                        Log.d("API CALL", userAsset.createdOn + "");

                        realm.setText(userAsset.realm);
                        userId.setText(userAsset.userID);
                        firstName.setText(userAsset.firstName);
                        lastName.setText(userAsset.lastName);
                        email.setText(userAsset.email);

                        if(userAsset.enabled) {
                            enabled.setText(getString(R.string.true_value));}
                        else {
                            enabled.setText(getString(R.string.false_value));
                        }

                        if(userAsset.serviceAccount) {
                            serviceAccount.setText(getString(R.string.true_value));
                        }
                        else {
                            serviceAccount.setText(getString(R.string.false_value));
                        }
                        userName.setText(userAsset.userName.toString());

                        long epochTime = userAsset.createdOn; // Replace this with your epoch time

                        // Create a Date object using the epoch time
                        Date date = new Date(epochTime);

                        // Define the date format
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                        // Format the date as a human-readable string
                        String humanReadableDate = dateFormat.format(date);
                        createOn.setText(humanReadableDate);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("API CALL", t.getMessage().toString());
                    }
                }
        );
    }
}