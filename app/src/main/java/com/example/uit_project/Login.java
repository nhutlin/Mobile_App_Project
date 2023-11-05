package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uit_project.api.ApiService;
import com.example.uit_project.model.ApiRespone;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private ImageButton back_btn;
    private Button login_btn;

    private TextView goToSignUp;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back_btn = findViewById(R.id.ic_back_login);
        login_btn = findViewById(R.id.btn_login);
        username = findViewById(R.id.input_username_login);
        password = findViewById(R.id.input_pass_login);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        goToSignUp = findViewById(R.id.label_signup);
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
                String user = username.getText().toString();
                String pass = password.getText().toString();

                ApiService authService;
                authService = ApiClient.getClient().create(ApiService.class);

                Call<ApiRespone> call = authService.getToken("openremote", user, pass, "password");
                call.enqueue(new Callback<ApiRespone>() {
                    @Override
                    public void onResponse(Call<ApiRespone> call, Response<ApiRespone> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(Login.this, "Log in successfull!", Toast.LENGTH_SHORT).show();
                            Intent goToDashboard = new Intent();
                            goToDashboard.setClass(Login.this, Dashboard.class);

                            startActivity(goToDashboard);
                        }


                    }
                    @Override
                    public void onFailure(Call<ApiRespone> call, Throwable t) {
                        Toast.makeText(Login.this, "Log in fail!", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}