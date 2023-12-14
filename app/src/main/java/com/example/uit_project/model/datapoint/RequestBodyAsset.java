package com.example.uit_project.model.datapoint;

import com.google.gson.annotations.SerializedName;

import okhttp3.MediaType;

public class RequestBodyAsset {
    @SerializedName("fromTimestamp")
    private long fromTimestamp;
    @SerializedName("toTimestamp")
    private long toTimestamp;
    @SerializedName("type")
    private String type;

    public RequestBodyAsset(long fromTimestamp, long toTimestamp, String type){

        this.fromTimestamp = fromTimestamp;
        this.toTimestamp = toTimestamp;
        this.type = type;
    }

}
