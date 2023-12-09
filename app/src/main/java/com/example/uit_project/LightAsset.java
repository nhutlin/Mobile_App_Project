package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uit_project.api.ApiService;
import com.example.uit_project.model.weather.WeatherAsset;

import org.w3c.dom.Text;

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
    private ImageView returnMap;
    private ImageView viewGraph;
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
        returnMap = findViewById(R.id.return_map);
        viewGraph = findViewById(R.id.view_graph);

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        user.setText(" " + username);

        returnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ApiService.apiService.getLightAsset("6iWtSbgqMQsVq8RPkJJ9vo", "Bearer " + GlobalVar.token)
                .enqueue(new Callback<com.example.uit_project.model.light.LightAsset>() {
                    @Override
                    public void onResponse(Call<com.example.uit_project.model.light.LightAsset> call, Response<com.example.uit_project.model.light.LightAsset> response) {
                        Log.d("API CALL", response.code()+"");
                        com.example.uit_project.model.light.LightAsset asset = response.body();

                        brightness_logo.setText(String.valueOf(asset.attributes.brightness.value));
                        brightness.setText(String.valueOf(asset.attributes.brightness.value));
                        email.setText(asset.attributes.email.value);
                        notes.setText(asset.attributes.notes.value);
                        colour_RGB.setText(asset.attributes.colourRGB.value);
                        colour_temperature.setText(String.valueOf(asset.attributes.colourTemperature.value) + "K");
                        tags.setText(asset.attributes.tags.value.toString());
                        if(asset.attributes.onOff.value) {
                            on_off.setText(getString(R.string.on));
                        } else {
                            on_off.setText(getString(R.string.off));
                        }
                    }

                    @Override
                    public void onFailure(Call<com.example.uit_project.model.light.LightAsset> call, Throwable t) {

                    }
                });

    }
}