package com.example.uit_project.model.map;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Default {
    @SerializedName("center")
    public List<Double> center;
    @SerializedName("zoom")
    public double zoom;
    @SerializedName("minZoom")
    public double minZoom;
    @SerializedName("maxZoom")
    public double maxZoom;

    @SerializedName("bounds")
    public List<Double> bounds;
    @SerializedName("geocodeUrl")
    String geocodeUrl;
}
