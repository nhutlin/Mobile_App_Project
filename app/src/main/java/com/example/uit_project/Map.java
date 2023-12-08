package com.example.uit_project;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;



import org.osmdroid.config.Configuration;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;


public class Map extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private Marker markerLight;
    private Marker markerWeather;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = (MapView) findViewById(R.id.map);

        // Set map zoom and map's position
        map.setTileSource(TileSourceFactory.MAPNIK);
        IMapController mapController = map.getController();
        mapController.setZoom(19.25);
        GeoPoint startPoint = new GeoPoint(10.869778736885038, 106.80280655508835);
        mapController.setCenter(startPoint);

        // Mark the weather assent to the map
        GeoPoint point = new GeoPoint(10.869778736885038, 106.80280655508835);
        markerWeather = new Marker(map);
        markerWeather.setPosition(point);

        // Set the icon for weather marker
        Resources res = getResources();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = (int) (44 * displayMetrics.density); // Adjust the width as needed
        int height = (int) (44 * displayMetrics.density); // Adjust the height as needed

        // Decode the bitmap with the desired size
        Bitmap markerBitmapWeather = BitmapFactory.decodeResource(res, R.drawable.ic_weather_marker);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(markerBitmapWeather, width, height, false);

        // Create a custom marker drawable
        CustomMarkerDrawable markerDrawableWeather = new CustomMarkerDrawable(res, resizedBitmap);
        markerWeather.setIcon(markerDrawableWeather);
        markerWeather.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        markerWeather.setInfoWindow(null);
        markerWeather.setTextLabelFontSize(50);
        map.getOverlays().add(markerWeather);

        Bitmap markerBitmapLight= BitmapFactory.decodeResource(res, R.drawable.ic_light_marker);
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

//        requestPermissionsIfNecessary(new String[]{
//                // if you need to show the current location, uncomment the line below
////                 Manifest.permission.ACCESS_FINE_LOCATION,
//                // WRITE_EXTERNAL_STORAGE is required in order to show the map
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//
//
//        });
        showInfo();
    }

    public void showInfo() {

    }
    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
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
                // Permission is not granted
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