package com.example.uit_project;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uit_project.api.ApiService;
import com.example.uit_project.model.ApiResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    private ImageButton back_btn;
    private Button login_btn;

    private TextView goToSignUp;
    private EditText username;
    private EditText password;
    private CheckBox isSave;
    private static final String SHARED_PREFS = "shared_Prefs";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String ISSAVE = "checkbox1";
    private String saveUserName;
    private String savePass;
    private boolean saveCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back_btn = findViewById(R.id.ic_back_login);
        login_btn = findViewById(R.id.btn_login);
        username = findViewById(R.id.input_username_login);
        password = findViewById(R.id.input_pass_login);
        isSave = findViewById(R.id.save_account);
        GlobalVar.view = (RelativeLayout)findViewById(R.id.login_scr);
        GlobalVar.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        goToSignUp = findViewById(R.id.signup_suggest);
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(Login.this, SignUp.class);

                startActivity(i);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSave.isChecked()) {
                    saveData();
                } else {
                    clearData();
                }

                String user = username.getText().toString();
                String pass = password.getText().toString();


                ApiService authService;
                authService = ApiClientLogin.getClient().create(ApiService.class);

                Call<ApiResponse> call = authService.getToken("openremote", user, pass, "password");
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if(response.isSuccessful()) {

                            ApiResponse apiResponse = response.body();

                            Intent i = new Intent();
                            i.setClass(Login.this, Map.class);
                            startActivity(i);
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(Login.this, getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        loadData();
        updateView();

    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, username.getText().toString());
        editor.putString(PASSWORD, password.getText().toString());
        editor.putBoolean(ISSAVE, isSave.isChecked());

        editor.apply();

    }
    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        saveUserName = sharedPreferences.getString(USERNAME, "");
        savePass = sharedPreferences.getString(PASSWORD, "");
        saveCheckBox = sharedPreferences.getBoolean(ISSAVE, false);

    }
    private void updateView() {
        Log.d("UPDATE DATA", "Data updated");
        username.setText(saveUserName);
        password.setText(savePass);
        isSave.setChecked(saveCheckBox);
    }

    private void clearData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, "");
        editor.putString(PASSWORD, "");
        editor.putBoolean(ISSAVE, false);

        editor.apply();
    }
}