package com.example.uit_project;
import com.example.uit_project.model.light.LightAsset;
import com.example.uit_project.model.weather.WeatherAsset;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uit_project.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAsset extends AppCompatActivity {
    private TextView temperature;
    private TextView temperature_info;
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

    ApiService apiService;
    //private String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDE2MjU2MzYsImlhdCI6MTcwMTUzOTIzNywiYXV0aF90aW1lIjoxNzAxNTM5MjM2LCJqdGkiOiI2NTM0ZWVkYS0zNGVmLTQyZmYtYjVmNC1mMTEyODRkZGJmYWMiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiZDRkMmIxZDEtMWEyYi00NjU2LTlmZWMtZDJmMGU1MTA1NWEzIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJkNGQyYjFkMS0xYTJiLTQ2NTYtOWZlYy1kMmYwZTUxMDU1YTMiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJGaXJzdCBOYW1lIExhc3QgbmFtZSIsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIiLCJnaXZlbl9uYW1lIjoiRmlyc3QgTmFtZSIsImZhbWlseV9uYW1lIjoiTGFzdCBuYW1lIiwiZW1haWwiOiJ1c2VyQGl4eGMuZGV2In0.RdfoGaY_Lcil1OZ9KCGteI4WDkVCVVCHRubCUpeYTvbrhmqnk8hG7jAHROkWN8WcWzEZAtETMCI7RvqttrCuHmATTra8Oz7eVOMZ5iw_uP3jlXBoOJOYKXSMPM2Al0bXVIcDjILN85y5s1DhWK4iIlQB5Cam7rE0FVtbBP4C5QpTFFgk79Vsh__GjUGuSbFXd0aGyhTa8t-dzou3jpcv7VJ_E3Aes9q3ucTYe9ue18Aw1-QOMWo2WhNqOnJ9Tt9splJ4WWI7Jfg5Sfv4-3BJiqD2jpUBt66wcjVT_fnpM5i-0kRqt5MR-fHzYU5WSoHbsExfZP7yvpePrXxYbLc10Q";
    //private String token = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJjYjIzZWEzMC1mOTViLTQxYTYtYjVjOC01ZDIyNjMyNGEyMjMifQ.eyJleHAiOjE3MDE5MjI3NDcsImlhdCI6MTcwMTgzNjM0NywianRpIjoiYmI2OTFjYzUtZGQzNi00NDNjLTk3OTktYWUzNWIyNGIxNmFmIiwiaXNzIjoiaHR0cHM6Ly91aW90Lml4eGMuZGV2L2F1dGgvcmVhbG1zL21hc3RlciIsImF1ZCI6Imh0dHBzOi8vdWlvdC5peHhjLmRldi9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJzdWIiOiI0YzM5NmNiYS0yOWZmLTRhYzEtYWVhYS1kYmFiNmM3Y2E4MmUiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiI1NTc2Njk1NC04YWQ0LTQwN2YtYTUxOC1jNzhkZGFlMjUwMzciLCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI1NTc2Njk1NC04YWQ0LTQwN2YtYTUxOC1jNzhkZGFlMjUwMzcifQ.a65fQHWd2ZbQm0Zs6eQMZNniWd2WPDj8bUcL168W9T8";
    //private String token = " eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDE1Mzg0MjAsImlhdCI6MTcwMTQ1MjY2MSwiYXV0aF90aW1lIjoxNzAxNDUyMDIwLCJqdGkiOiJmNWE0YTliMC03YTFmLTQzZmEtYmQ5NC05ZTNkNjZkNmU1NGQiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiNTQyZGUwMzctZTllZi00N2U3LTljZTItZjVmMzg2NzFiMzJhIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwic2lkIjoiNTQyZGUwMzctZTllZi00N2U3LTljZTItZjVmMzg2NzFiMzJhIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiRmlyc3QgTmFtZSBMYXN0IG5hbWUiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6IkZpcnN0IE5hbWUiLCJmYW1pbHlfbmFtZSI6Ikxhc3QgbmFtZSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.L713xLglorcTUAwJw15SZUt6GSzDj9X9YuV4bmMDIgQJL4dL9ZMMDe4fTBmHKgiagDLVfgsPoX6I3sL2zjsb1Xe7NGstzMzggR6mPQES21h0VxTuEVyuPDOm6amUwN-NkWVEjClajsRNuu6ENSlUEoGx8KUBaUNrb-VirNvGTVbPX-E4DvcuY0C2jGbROI37LcGOFgNAxVwae3Uczg8KEeUBkQ3HTmqrl9r1mOxo4fBmafSP-ZqHM-1Pc1V7s4WS-gRmHFhWZhFHhSMuFVdd_H3GSpixXLwdEeVMu4KzbrM997xMXoSrzWtLxTPXCPMx70m1Ea4N9a3EXPadRhr25Q";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_asset);

        temperature_info = findViewById(R.id.temperature_info);
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

        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
//        int hour12hrs = calendar.get(Calendar.HOUR);
//        int minutes = calendar.get(Calendar.MINUTE);
//        int seconds = calendar.get(Calendar.SECOND);



        ApiService.apiService.getWeatherAsset("5zI6XqkQVSfdgOrZ1MyWEf", "Bearer" + GlobalVar.token)
                .enqueue(new Callback<WeatherAsset>() {
                    @Override
                    public void onResponse(Call<WeatherAsset> call, Response<WeatherAsset> response) {
                        Log.d("API CALL", response.code()+"");
                        WeatherAsset asset = response.body();
                        int valueTemperature = (int) asset.attributes.temperature.value;
                        // get header value
                        if(hour24hrs >= 6 && hour24hrs <= 18) {
                            if(valueTemperature > 27) {
                                main_back_weather.setBackgroundResource(R.drawable.back_sunny);
//                                iconWeather.setImageResource(R.drawable.sunny);

                            } else if(valueTemperature <= 27 && valueTemperature >= 25){
                                main_back_weather.setBackgroundResource(R.drawable.back_cloudy_sunny);
//                                iconWeather.setImageResource(R.drawable.cloudy_sunny);
                            }
                            else {
                                main_back_weather.setBackgroundResource(R.drawable.back_cloudy);
//                                iconWeather.setImageResource(R.drawable.cloudy);
                            }

                        } else {
                            if(valueTemperature > 25) {
                                main_back_weather.setBackgroundResource(R.drawable.back_night);
                            } else if(valueTemperature <= 25) {
                                main_back_weather.setBackgroundResource(R.drawable.back_night_cloud);
                            }
                        }

                        temperature_info.setText(String.valueOf(valueTemperature) + "\u00B0");
                        place.setText(asset.attributes.place.value.toString());

                        manufacturer.setText(asset.attributes.manufacturer.value);
                        temperature.setText(String.valueOf(valueTemperature) + "\u00B0");
                        humidity.setText(String.valueOf(asset.attributes.humidity.value) + "%");
                        rainfall.setText(String.valueOf(asset.attributes.rainFall.value) + " mm");
                        notes.setText(asset.attributes.notes.value);
                        wind_direction.setText(String.valueOf(asset.attributes.windDirection.value));
                        wind_speed.setText(String.valueOf(asset.attributes.windSpeed.value) + " km/h");
                        tags.setText(asset.attributes.tags.value.toString());

                    }

                    @Override
                    public void onFailure(Call<WeatherAsset> call, Throwable t) {

                    }
                });

    }
}