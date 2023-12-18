package com.example.uit_project.model.map;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;


import com.example.uit_project.CustomMarkerDrawable;
import com.example.uit_project.GlobalVar;
import com.example.uit_project.LightAsset;
import com.example.uit_project.LoadingAlert;
import com.example.uit_project.R;
import com.example.uit_project.UserProfile;
import com.example.uit_project.WeatherAsset;
import com.example.uit_project.api.APIService;
import com.example.uit_project.model.weather.WeatherAssetResponse;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;



import org.osmdroid.config.Configuration;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Map extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private Marker markerLight;
    private Marker markerWeather;
    private GeoPoint point;
    private Dialog dialog;
    private Button closePopup;
    private Button viewDetails;
    private String username;
    private TextView brightness;
    private TextView colour_temperature;
    private TextView on_off;
    private TextView humidity;
    private TextView manufacturer;
    private IMapController mapController;
    private TextView rainfall;
    private TextView temperature;
    private ImageButton back;
    private APIService apiService;
    private GeoPoint startPoint;
    private LoadingAlert loadingAlert;


    private double zoom = 0;
    private double latitudeMap = 0;
    private double longitudeMap = 0;
    private ImageButton profile;

    private double latitudeWeather;
    private double longitudeWeather;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        Intent intent = this.getIntent();
        username = intent.getStringExtra("Username");

        map = (MapView) findViewById(R.id.map);

        // Set map zoom and map's position
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);


        APIService.apiService.getMapSetting("Bearer " + GlobalVar.token)
                .enqueue(new Callback<MapSetting>() {
                    @Override
                    public void onResponse(Call<MapSetting> call, Response<MapSetting> response) {
                        Log.d("API CALL", String.valueOf(response.code()));
                        if(response.isSuccessful()) {
                            MapSetting mapSetting = response.body();

                            assert mapSetting != null;
                            longitudeMap = mapSetting.options.aDefault.center.get(0);
                            latitudeMap = mapSetting.options.aDefault.center.get(1);
                            mapController = map.getController();
                            mapController.setZoom(mapSetting.options.aDefault.zoom);
                            startPoint = new GeoPoint(latitudeMap, longitudeMap);
                            mapController.setCenter(startPoint);
                            map.setMaxZoomLevel(mapSetting.options.aDefault.maxZoom);
                            map.setMinZoomLevel(mapSetting.options.aDefault.minZoom);

                            Log.d("TEST GEO", "Longitude: " + longitudeMap);
                            Log.d("TEST GEO", "Latitude: " + latitudeMap);

                        } else {
                            Log.d("API CALL", String.valueOf(response.code()));
                            zoom = 19.25;
                            latitudeMap = 10.869778736885038;
                            longitudeMap = 106.80280655508835;
                            IMapController mapController = map.getController();
                            mapController.setZoom(zoom);
                            GeoPoint startPoint = new GeoPoint(latitudeMap, longitudeMap);
                            mapController.setCenter(startPoint);
                            map.setMaxZoomLevel(16.0);
                            map.setMinZoomLevel(0.0);
                        }
                    }

                    @Override
                    public void onFailure(Call<MapSetting> call, Throwable t) {
                        Log.d("API CALL", t.getMessage().toString());
                        Toast.makeText(Map.this, getString(R.string.load_map_error), Toast.LENGTH_SHORT).show();
                    }
                });


        back = findViewById(R.id.ic_back);
        profile = findViewById(R.id.ic_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Map.this, UserProfile.class);
                startActivity(intent);
            }
        });

        // Mark the weather assent to the map
        point = new GeoPoint(10.869778736885038, 106.80280655508835);
        markerWeather = new Marker(map);
        markerWeather.setPosition(point);

        // Set the icon for weather marker
        Resources res = getResources();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = (int) (44 * displayMetrics.density); // Adjust the width as needed
        int height = (int) (44 * displayMetrics.density); // Adjust the height as needed

        // Decode the bitmap with the desired size
        Bitmap markerBitmapWeather = BitmapFactory.decodeResource(res, R.drawable.ic_weather_marker2);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(markerBitmapWeather, width, height, false);

        // Create a custom marker drawable
        CustomMarkerDrawable markerDrawableWeather = new CustomMarkerDrawable(res, resizedBitmap);
        markerWeather.setIcon(markerDrawableWeather);
        markerWeather.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        markerWeather.setInfoWindow(null);
        markerWeather.setTextLabelFontSize(50);
        map.getOverlays().add(markerWeather);

        Bitmap markerBitmapLight = BitmapFactory.decodeResource(res, R.drawable.ic_light_marker);
        Bitmap resizedBitLight = Bitmap.createScaledBitmap(markerBitmapLight, width, height, false);

        // Create a custom marker drawable
        CustomMarkerDrawable markerDrawableLight = new CustomMarkerDrawable(res, resizedBitLight);

        // Mark the light assent to the map
        GeoPoint point2 = new GeoPoint(10.869905172970164, 106.80345028525176);
        markerLight = new Marker(map);
        markerLight.setPosition(point2);
        markerLight.setIcon(markerDrawableLight);
        markerLight.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        markerLight.setInfoWindow(null);
        map.getOverlays().add(markerLight);

        

        dialog = new Dialog(this);
        Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.BOTTOM);

        showInfo();
    }
    public void showInfo() {
        markerWeather.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                dialog.setContentView(R.layout.weather_popup);
                dialog.show();
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Log.v("SHOW DIALOG", "SUCCESS");

                humidity = dialog.findViewById(R.id.humidity_value);
                manufacturer = dialog.findViewById(R.id.manufacturer_value);
                rainfall = dialog.findViewById(R.id.rainfall_value);
                temperature = dialog.findViewById(R.id.temperature_value);
                closePopup = dialog.findViewById(R.id.close_popup);
                viewDetails = dialog.findViewById(R.id.view_details);

                APIService.apiService.getWeatherAsset("5zI6XqkQVSfdgOrZ1MyWEf",
                                "Bearer " + GlobalVar.token)
                        .enqueue(new Callback<WeatherAssetResponse>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(Call<WeatherAssetResponse> call, Response<WeatherAssetResponse> response) {
                                Log.d("API CALL", String.valueOf(response.code()));
                                if(response.isSuccessful()) {
                                    WeatherAssetResponse asset = response.body();
                                    humidity.setText(asset.attributes.humidity.value + "%");
                                    manufacturer.setText(asset.attributes.manufacturer.value);
                                    rainfall.setText(String.valueOf(asset.attributes.rainFall.value));
                                    temperature.setText(asset.attributes.temperature.value + "\u2103");
                                }
                                else {
                                    Toast.makeText(Map.this, getString(R.string.get_permission),
                                            Toast.LENGTH_SHORT).show();
                                    humidity.setText("--");
                                    manufacturer.setText("--");
                                    rainfall.setText("--");
                                    temperature.setText("--");
                                }

                            }
                            @Override
                            public void onFailure(Call<WeatherAssetResponse> call, Throwable t) {
                                Log.d("API CALL", t.getMessage().toString());
                            }
                        });
                closePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        Log.v("HIDE DIALOG", "SUCCESS");
                    }
                });

                viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.putExtra("Username", username);
                        i.setClass(Map.this, WeatherAsset.class);
                        startActivity(i);
                    }
                });
                return true;
            }
        });
        markerLight.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                dialog.setContentView(R.layout.light_popup);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                Log.v("SHOW DIALOG", "SUCCESS");

                brightness = dialog.findViewById(R.id.brightness_value);
                colour_temperature = dialog.findViewById(R.id.colourTemperature_value);
                on_off = dialog.findViewById(R.id.onOff_value);
                closePopup = dialog.findViewById(R.id.close_popup);
                viewDetails = dialog.findViewById(R.id.view_details);

                APIService.apiService.getLightAsset("6iWtSbgqMQsVq8RPkJJ9vo", "Bearer " + GlobalVar.token)
                        .enqueue(new Callback<com.example.uit_project.model.light.LightAsset>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(Call<com.example.uit_project.model.light.LightAsset> call, Response<com.example.uit_project.model.light.LightAsset> response) {
                                Log.d("API CALL", String.valueOf(response.code()));
                                if(response.isSuccessful()) {
                                    com.example.uit_project.model.light.LightAsset asset = response.body();

                                    brightness.setText(asset.attributes.brightness.value + "%");
                                    colour_temperature.setText(asset.attributes.colourTemperature.value + "K");
                                    if(asset.attributes.onOff.value) {
                                        on_off.setText(getString(R.string.on));
                                    } else {
                                        on_off.setText(getString(R.string.off));
                                    }
                                } else {
                                    Toast.makeText(Map.this, getString(R.string.get_permission),
                                            Toast.LENGTH_SHORT).show();
                                    brightness.setText("--");
                                    colour_temperature.setText("--");
                                    on_off.setText("--");
                                }

                            }
                            @Override
                            public void onFailure(Call<com.example.uit_project.model.light.LightAsset> call, Throwable t) {
                            }
                        });
                closePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        Log.v("HIDE DIALOG", "SUCCESS");
                    }
                });

                viewDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.putExtra("Username", username);
                        i.setClass(Map.this, LightAsset.class);
                        startActivity(i);
                    }
                });
                return true;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}