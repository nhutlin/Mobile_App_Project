package com.example.uit_project.model.map;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Default {
    @SerializedName("center")
    List<Float> center;

    @SerializedName("bounds")
    List<Float> bounds;
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
