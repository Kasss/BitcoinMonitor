package com.devkitchen.kas.bitcoinmonitor.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CurrentGetCoin {

    @SerializedName("bpi")
    @Expose
    private JsonObject bpi;
    @SerializedName("disclaimer")
    @Expose
    private String disclaimer;
    @SerializedName("time")
    @Expose
    private CurrentCoinTime time;

    public CurrentGetCoin() {

    }

    public CurrentGetCoin(JsonObject bpi, String disclaimer, CurrentCoinTime time) {
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

    public CurrentCoinTime getTime() {
        return time;
    }

    public void setTime(CurrentCoinTime time) {
        this.time = time;
    }

}

