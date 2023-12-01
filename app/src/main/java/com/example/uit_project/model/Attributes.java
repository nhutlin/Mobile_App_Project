package com.example.uit_project.model;

import com.google.gson.annotations.SerializedName;

public class Attributes {
    public SunIrradiance sunIrradiance;
    @SerializedName("rainfall")
    public RainFall rainFall;

    public Notes notes;
    public UVIndex uvIndex;
    public SunAzimuth sunAzimuth;
    public SunZenith sunZenith;

    public Tags tags;
    public Manufacturer manufacturer;
    public Temperature temperature;
    public Humidity humidity;
    @SerializedName("location")
    public Location location;
    public Place place;
    public WindDirection windDirection;
    public WindSpeed windSpeed;
    public SunAltitude sunAltitude;
}
