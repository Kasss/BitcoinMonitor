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
import com.devkitchen.kas.datetimepicker.popwindow.DatePickerPopWin;
import com.devkitchen.kas.datetimepicker.popwindow.WheelPickerPopWin;

public class Utilities {

    private ArrayList<String> currencyList = new ArrayList<>();

    private String returnText = "";
    private String returnCurrency = "";

    public String getDate(Activity activity) {
        final Calendar currentDateAndTime = Calendar.getInstance();
        final Calendar selectedDate = (Calendar) currentDateAndTime.clone();
        final Calendar minDate = (Calendar) currentDateAndTime.clone();
        final Calendar maxDate = (Calendar) currentDateAndTime.clone();
        maxDate.set(Calendar.YEAR, currentDateAndTime.get(Calendar.YEAR));
        minDate.set(Calendar.YEAR, 2002);
        if (activity != null) {
            DatePickerPopWin popWin = new DatePickerPopWin.Builder(activity.getBaseContext(), new DatePickerPopWin.OnDatePickedListener() {
                @Override
                public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                    Calendar calendar = (Calendar) currentDateAndTime.clone();
                    calendar.set(year, month - 1, day);
                    returnText = dateDesc;
                }
            }).textConfirm("ОК")
                    .textCancel("Отмена")
                    .btnTextSize(16)
                    .viewTextSize(18)
                    .showDayMonthYear(true)
                    .colorCancel(Color.parseColor("#999999")) //color of cancel button
                    .colorConfirm(Color.parseColor("#2d095c"))
                    .minYear(minDate.get(Calendar.YEAR)) //min year in loop
                    .maxYear(maxDate.get(Calendar.YEAR)) // max year in loop
                    .minDate(minDate)
                    .maxDate(maxDate)
                    .dateChose(selectedDate)
                    .build();
            popWin.showPopWin(activity);
        }

        Date date = currentDateAndTime.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(date);
        return returnText;
    }

    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap();
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


    public String getCurrency(Activity activity) {

        currencyList.add("USD");
        currencyList.add("EUR");
        currencyList.add("GBP");
        currencyList.add("KZT");

        if (activity != null) {

            WheelPickerPopWin pickerPopWin = new WheelPickerPopWin.Builder(activity.getBaseContext(), new WheelPickerPopWin.OnWheelPickedListener() {
                @Override
                public void onWheelPickCompleted(String value) {
                   returnCurrency = value;
                }
            }).textCenterTextView("Валюта")
                    .textConfirm("Готово") //text of confirm button
                    .textCancel("Отмена") //text of cancel button
                    .btnTextSize(16) // button text size
                    .viewTextSize(20) // pick view text size
                    .colorCancel(Color.parseColor("#999999")) //color of cancel button
                    .colorConfirm(Color.parseColor("#2d095c"))//color of confirm button
                    .wheelList(currencyList)
                    .itemChose(0) // date chose when init popwindow
                    .build();
            pickerPopWin.showPopWin(activity);

        }
        return returnCurrency;
    }


}
