package com.devkitchen.kas.bitcoinmonitor.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.HashMap;

public class GetCoin {

    @SerializedName("bpi")
    @Expose
    private JsonObject bpi;
    @SerializedName("disclaimer")
    @Expose
    private String disclaimer;
    @SerializedName("time")
    @Expose
    private Time time;

    public GetCoin() {

    }

    public GetCoin(JsonObject bpi, String disclaimer, Time time) {
        this.bpi = bpi;
        this.disclaimer = disclaimer;
        this.time = time;
    }


    public JsonObject getBpi() {
        return bpi;
    }

    public void setBpi(JsonObject bpi) {
        this.bpi = bpi;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

}
