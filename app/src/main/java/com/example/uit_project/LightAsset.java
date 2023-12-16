package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uit_project.api.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LightAsset extends AppCompatActivity {
    private String username;
    private TextView brightness_logo;

    private TextView brightness;
    private TextView email;
    private TextView notes;
    private TextView colour_RGB;
    private TextView colour_temperature;

    private TextView on_off;
    private TextView tags;

    private TextView user;
    private ImageButton returnMap;
    private ImageButton viewGraph;
    private ImageButton viewProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_asset);

        user = findViewById(R.id.username);
        brightness_logo = findViewById(R.id.brightness_logo);
        brightness = findViewById(R.id.value_brightness);
        email = findViewById(R.id.value_email);
        notes = findViewById(R.id.value_notes);
        colour_RGB = findViewById(R.id.value_colourRGB);
        colour_temperature = findViewById(R.id.value_colourTemperature);
        on_off = findViewById(R.id.value_onOff);
        tags = findViewById(R.id.value_tags);
        returnMap = findViewById(R.id.return_map_light);
        viewGraph = findViewById(R.id.view_graph_light);
        viewProfile = findViewById(R.id.view_profile_light);

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        user.setText(" " + username);

        returnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LightAsset.this, ChartTest.class);
                startActivity(intent);
            }
        });
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LightAsset.this, UserProfile.class);
                startActivity(intent);
            }
        });

        APIService.apiService.getLightAsset("6iWtSbgqMQsVq8RPkJJ9vo", "Bearer " + GlobalVar.token)
                .enqueue(new Callback<com.example.uit_project.model.light.LightAsset>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(Call<com.example.uit_project.model.light.LightAsset> call, Response<com.example.uit_project.model.light.LightAsset> response) {
                        Log.d("API CALL", String.valueOf(response.code()));
                        if(response.isSuccessful()) {
                            com.example.uit_project.model.light.LightAsset asset = response.body();

                            assert asset != null;
                            brightness_logo.setText(asset.attributes.brightness.value + "%");
                            brightness.setText(asset.attributes.brightness.value + "%");
                            email.setText(asset.attributes.email.value);
                            notes.setText(asset.attributes.notes.value);
                            colour_RGB.setText(asset.attributes.colourRGB.value);
                            colour_temperature.setText(asset.attributes.colourTemperature.value + "K");
                            tags.setText(asset.attributes.tags.value.toString());
                            if(asset.attributes.onOff.value) {
                                on_off.setText(getString(R.string.on));
                            } else {
                                on_off.setText(getString(R.string.off));
                            }
                        } else {
                            Toast.makeText(LightAsset.this, getString(R.string.get_permission),
                                    Toast.LENGTH_SHORT).show();
                            brightness_logo.setText("--");
                            brightness.setText("--");
                            email.setText("--");
                            notes.setText("--");
                            colour_RGB.setText("--");
                            colour_temperature.setText("--");
                            tags.setText("--");
                            on_off.setText("--");
                        }

                    }

                    @Override
                    public void onFailure(Call<com.example.uit_project.model.light.LightAsset> call, Throwable t) {
                        Log.d("API CALL", t.getMessage().toString());                 }
                });

    }
}