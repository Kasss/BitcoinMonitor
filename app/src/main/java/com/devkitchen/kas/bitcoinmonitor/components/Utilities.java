package com.devkitchen.kas.bitcoinmonitor.components;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;


/**
 * created by Kassen Dauren 25.10.2018
 */

public class Utilities {

    public final static String DEFAULT_START_DATE = "2013-09-01";
    public final static String DEFAULT_END_DATE = "2013-09-05";
    public final static String DEFAULT_CURRENCY = "USD";

    private String returnText = "";
    private String returnCurrency = "";

    /**
     * Method that allows to pull out keys as String from JsonObject
     *
     * @param json
     * @return ArrayList
     * @author Kassen Dauren
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
     * Method that allows to pull out values as String from JsonObject
     *
     * @param json
     * @return ArrayList
     * @author Kassen Dauren
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

    /**
     * Method that allows to pull out values as HashMap from JSONObject
     *
     * @param object
     * @return HashMap map
     * @author Kassen Dauren
     */
    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<>();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    /**
     * Method that allows to pull out values value of JSON
     *
     * @param json
     * @return null, Object json, HashMap, ArrayList
     * @author Kassen Dauren
     */
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

    /**
     * Method that recreates values to list from JSONArray
     *
     * @param array
     * @return list
     * @author Kassen Dauren
     */
    public static ArrayList toList(JSONArray array) throws JSONException {
        ArrayList list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    /**
     * Method that recreates values to list from JSONArray
     *
     * @param dateConvert
     * @return date
     * @author Kassen Dauren
     */
    public static Date convertDate(String dateConvert) {
        Date date = Calendar.getInstance().getTime();
        try {
            date = new SimpleDateFormat("yyyy-mm-dd").parse(dateConvert);
        } catch (ParseException e) {
            e.getLocalizedMessage();
        }
        return date;
    }


}
