package com.example.uit_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uit_project.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherAsset extends AppCompatActivity {
    private TextView temperature;
    private TextView temperature_logo;
    private TextView place;
    private ImageView iconWeather;

    private RelativeLayout main_back_weather;
    private TextView manufacturer;
    private TextView notes;
    private TextView humidity;
    private TextView rainfall;
    private TextView wind_direction;
    private TextView wind_speed;
    private TextView uv;
    private TextView tags;
    private ImageView returnMap;
    private ImageView viewGraph;
    private TextView sun_altitude;
    private TextView sun_azimuth;
    private TextView sun_irradiance;
    private TextView sun_zenith;

    private String username;
    private TextView user;

    ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_asset);

        user = findViewById(R.id.username);
        temperature_logo = findViewById(R.id.temperature_logo);
        place = findViewById(R.id.place);
        iconWeather = findViewById(R.id.icon_weather);
        main_back_weather = (RelativeLayout) findViewById(R.id.main_back_weather);
        manufacturer = findViewById(R.id.value_manufacturer);
        notes = findViewById(R.id.value_notes);
        rainfall = findViewById(R.id.value_rainfall);
        humidity = findViewById(R.id.value_humidity);
        wind_direction = findViewById(R.id.value_wind_direction);
        wind_speed = findViewById(R.id.value_wind_speed);
        temperature = findViewById(R.id.value_temperature);
        uv = findViewById(R.id.value_uv_index);
        tags = findViewById(R.id.value_tags);
        sun_altitude = findViewById(R.id.value_sunAltitude);
        sun_azimuth = findViewById(R.id.value_sunAzimuth);
        sun_irradiance = findViewById(R.id.value_sunIrradiance);
        sun_zenith = findViewById(R.id.value_sunZenith);
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
        viewGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(WeatherAsset.this, Chart.class);
                startActivity(i);
            }
        });

        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
//        int hour12hrs = calendar.get(Calendar.HOUR);
//        int minutes = calendar.get(Calendar.MINUTE);
//        int seconds = calendar.get(Calendar.SECOND);

        ApiService.apiService.getWeatherAsset("5zI6XqkQVSfdgOrZ1MyWEf", "Bearer " + GlobalVar.token)
                .enqueue(new Callback<com.example.uit_project.model.weather.WeatherAsset>() {
                    @Override
                    public void onResponse(Call<com.example.uit_project.model.weather.WeatherAsset> call, Response<com.example.uit_project.model.weather.WeatherAsset> response) {
                        Log.d("API CALL", response.code()+"");
                        com.example.uit_project.model.weather.WeatherAsset asset = response.body();

                        int valueTemperature = (int) asset.attributes.temperature.value;
                        // get header value
                        if(hour24hrs >= 6 && hour24hrs <= 18) {
                            if(valueTemperature > 27) {
                                iconWeather.setImageResource(R.drawable.sunny);
                            }
                            else if(valueTemperature <= 27 && valueTemperature >= 25){
                                iconWeather.setImageResource(R.drawable.cloudy_sunny);
                            }
                            else {
                              iconWeather.setImageResource(R.drawable.cloudy);
                            }
                        }
                        else {
                            if(valueTemperature > 25) {
                                iconWeather.setImageResource(R.drawable.moon);
                            } else if(valueTemperature <= 25) {
                                iconWeather.setImageResource(R.drawable.cloud_night);
                            }
                        }
                        if(asset.attributes.rainFall.value >= 5.0) {
                            iconWeather.setImageResource(R.drawable.rain);
                        }
                        temperature_logo.setText(String.valueOf(valueTemperature) + "\u00B0");
                        place.setText(asset.attributes.place.value.toString());

                        manufacturer.setText(asset.attributes.manufacturer.value);
                        temperature.setText(String.valueOf(valueTemperature) + "\u2103");
                        humidity.setText(asset.attributes.humidity.value + "%");
                        rainfall.setText(asset.attributes.rainFall.value + " mm");
                        notes.setText(asset.attributes.notes.value);
                        wind_direction.setText(String.valueOf(asset.attributes.windDirection.value));
                        wind_speed.setText(asset.attributes.windSpeed.value + " km/h");
                        tags.setText(asset.attributes.tags.value.toString());
                        sun_altitude.setText(asset.attributes.sunAltitude.value);
                        sun_azimuth.setText(asset.attributes.sunAzimuth.value);
                        sun_irradiance.setText(asset.attributes.sunIrradiance.value);
                    }

                    @Override
                    public void onFailure(Call<com.example.uit_project.model.weather.WeatherAsset> call, Throwable t) {

                    }
                });

    }
}