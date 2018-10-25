package com.devkitchen.kas.bitcoinmonitor.components;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.devkitchen.kas.datetimepicker.popwindow.DatePickerPopWin;
import com.devkitchen.kas.datetimepicker.popwindow.WheelPickerPopWin;
import com.google.gson.JsonObject;

public class Utilities {

    public final static String DEFAULT_START_DATE = "2013-09-01";
    public final static String DEFAULT_END_DATE = "2013-09-05";
    public final static String DEFAULT_CURRENCY = "USD";

    private String returnText = "";
    private String returnCurrency = "";

    /**
     * Method that allows to pull out keys as String
     *
     * @param json
     */
    public static ArrayList<String> getKeys(JsonObject json) {
        ArrayList<String> arrayList = new ArrayList<>();
        Set set = json.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            arrayList.add(String.valueOf(entry.getKey()));
        }
        return arrayList;
    }

    /**
     * Method that allows to pull out values as String
     *
     * @param json
     */
    public static ArrayList<String> getValues(JsonObject json) {
        ArrayList<String> arrayList = new ArrayList<>();
        Set set = json.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            arrayList.add(String.valueOf(entry.getValue()));
        }
        return arrayList;
    }

    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<>();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return toMap((JSONObject) json);
        } else if (json instanceof JSONArray) {
            return toList((JSONArray) json);
        } else {
            return json;
        }
    }

    public static ArrayList toList(JSONArray array) throws JSONException {
        ArrayList list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }


}
