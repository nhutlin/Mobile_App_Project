package com.example.uit_project.model.map;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Default {
    @SerializedName("center")
    List<Double> center;

    @SerializedName("bounds")
    List<Double> bounds;
    @SerializedName("zoom")
    int zoom;
    @SerializedName("minZoom")
    int minZoom;
    @SerializedName("maxZoom")
    int maxZoom;
    @SerializedName("boxZoom")
    boolean boxZoom;

    @SerializedName("geocodeUrl")
    String geocodeUrl;
}
