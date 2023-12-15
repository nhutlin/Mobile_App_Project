package com.example.uit_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uit_project.api.APIService;
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
    private ImageButton edit;

    private Button logOut;
    private ImageButton back;
    int background = 0;

    private Dialog dialog;
    private ImageView avatar;
    private ImageView ava1;
    private ImageView ava2;
    private ImageView ava3;
    private ImageView ava4;
    private ImageView ava5;
    private ImageView ava6;
    private Button ok;

    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;

    private CheckBox checkBox6;




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
        avatar = findViewById(R.id.profile_picture);
        dialog = new Dialog(this);
        dialog.getWindow().setGravity(Gravity.CENTER);

        edit = findViewById(R.id.edit_avatar);

        avatar.setImageResource(GlobalVar.drawableProfile);
        if(GlobalVar.drawableProfile == 0) {
            avatar.setImageResource(R.drawable.profile_picture);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.choose_avatar_popup);
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ava1 = dialog.findViewById(R.id.ava_1);
                ava2 = dialog.findViewById(R.id.ava_2);
                ava3 = dialog.findViewById(R.id.ava_3);
                ava4 = dialog.findViewById(R.id.ava_4);
                ava5 = dialog.findViewById(R.id.ava_5);
                ava6 = dialog.findViewById(R.id.ava_6);


                checkBox1 = dialog.findViewById(R.id.check1);
                checkBox2 = dialog.findViewById(R.id.check2);
                checkBox3 = dialog.findViewById(R.id.check3);
                checkBox4 = dialog.findViewById(R.id.check4);
                checkBox5 = dialog.findViewById(R.id.check5);
                checkBox6 = dialog.findViewById(R.id.check6);

                ok = dialog.findViewById(R.id.btn_ok);

                checkBox1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(true);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        checkBox5.setChecked(false);
                        checkBox6.setChecked(false);

                    }
                });

                checkBox2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(true);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        checkBox5.setChecked(false);
                        checkBox6.setChecked(false);
                    }
                });
                checkBox3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(true);
                        checkBox4.setChecked(false);
                        checkBox5.setChecked(false);
                        checkBox6.setChecked(false);
                    }
                });

                checkBox4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(true);
                        checkBox5.setChecked(false);
                        checkBox6.setChecked(false);
                    }
                });

                checkBox5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        checkBox6.setChecked(false);
                        checkBox5.setChecked(true);
                    }
                });

                checkBox6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        checkBox6.setChecked(true);
                        checkBox5.setChecked(false);
                    }
                });

                ava1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(true);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        checkBox5.setChecked(false);
                        checkBox6.setChecked(false);
                    }
                });
                ava2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(true);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        checkBox5.setChecked(false);
                        checkBox6.setChecked(false);
                    }
                });
                ava3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(true);
                        checkBox4.setChecked(false);
                        checkBox5.setChecked(false);
                        checkBox6.setChecked(false);
                    }
                });

                ava4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(true);
                        checkBox5.setChecked(false);
                        checkBox6.setChecked(false);
                    }
                });
                ava5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        checkBox5.setChecked(true);
                        checkBox6.setChecked(false);
                    }
                });
                ava6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkBox1.setChecked(false);
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                        checkBox4.setChecked(false);
                        checkBox5.setChecked(false);
                        checkBox6.setChecked(true);
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkBox1.isChecked()) {
                            avatar.setImageResource(R.drawable.ic_avatar1);
                            GlobalVar.drawableProfile = R.drawable.ic_avatar1;
                        }
                        else if(checkBox2.isChecked()) {
                            avatar.setImageResource(R.drawable.ic_avatar2);
                            GlobalVar.drawableProfile = R.drawable.ic_avatar3;
                        }
                        else if(checkBox3.isChecked()) {
                            avatar.setImageResource(R.drawable.ic_avatar3);
                            GlobalVar.drawableProfile = R.drawable.ic_avatar3;
                        }
                        else if(checkBox4.isChecked()) {
                            GlobalVar.drawableProfile = R.drawable.ic_avatar4;
                            avatar.setImageResource(R.drawable.ic_avatar4);
                        }
                        else if(checkBox5.isChecked()) {
                            GlobalVar.drawableProfile = R.drawable.ic_avatar5;
                            avatar.setImageResource(R.drawable.ic_avatar5);
                        }else if(checkBox6.isChecked()) {
                            GlobalVar.drawableProfile = R.drawable.ic_avatar6;
                            avatar.setImageResource(R.drawable.ic_avatar6);
                        }
                        dialog.cancel();
                    }
                });


            }
        });


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

        APIService.apiService.getUser("Bearer " + GlobalVar.token).enqueue(
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