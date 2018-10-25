package com.devkitchen.kas.bitcoinmonitor.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devkitchen.kas.bitcoinmonitor.R;
import com.devkitchen.kas.bitcoinmonitor.components.Utilities;
import com.devkitchen.kas.bitcoinmonitor.models.GetCoin;
import com.devkitchen.kas.datetimepicker.popwindow.DatePickerPopWin;
import com.devkitchen.kas.datetimepicker.popwindow.WheelPickerPopWin;
import com.github.mikephil.charting.charts.BarChart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainViewInterface {


    @BindView(R.id.enter_currency_value)
    TextView currencyText;

    @BindView(R.id.enter_start)
    EditText startEditText;

    @BindView(R.id.enter_end)
    EditText endEditText;

    @BindView(R.id.button_show)
    Button showResult;

    @BindView(R.id.graph_bar_chart)
    BarChart barChart;

    private String startDate, endDate, currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //currencyText = findViewById(R.id.enter_currency_value);
        currencyText.setOnClickListener(this);
        startEditText.setOnClickListener(this);
        endEditText.setOnClickListener(this);
        showResult.setOnClickListener(this);

    }


    @Override
    public void showToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void displayCoins(GetCoin coin) {

    }

    @Override
    public void displayError(String e) {
        showToast(e);
    }

    @Override
    public void onClick(View view) {
        if (view == startEditText) {
            //startDate = utility.getDate(this);
            //Utilities.getCurrency(this);
            getDate();
        }
        if (view == endEditText) {
            //startDate = utility.getDate(this);
            getDate();
        }
        if (view == currencyText) {
            //currency = utility.getCurrency(this);
            getCurrency();
        }
        if (view == showResult) {

        }
    }


    public void getCurrency() {
        ArrayList<String> currencyList = new ArrayList<>();
        currencyList.add("USD");
        currencyList.add("EUR");
        currencyList.add("GBP");
        currencyList.add("KZT");

        WheelPickerPopWin pickerPopWin = new WheelPickerPopWin.Builder(this, new WheelPickerPopWin.OnWheelPickedListener() {
            @Override
            public void onWheelPickCompleted(String value) {
                //returnCurrency = value;
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
        pickerPopWin.showPopWin(this);
        //return returnCurrency;
    }

    public void getDate() {

        final Calendar currentDateAndTime = Calendar.getInstance();
        final Calendar selectedDate = (Calendar) currentDateAndTime.clone();
        final Calendar minDate = (Calendar) currentDateAndTime.clone();
        final Calendar maxDate = (Calendar) currentDateAndTime.clone();
        maxDate.set(Calendar.YEAR, currentDateAndTime.get(Calendar.YEAR));
        minDate.set(Calendar.YEAR, 2010);
        minDate.set(Calendar.MONTH, 7);
        minDate.set(Calendar.DAY_OF_MONTH, 17);
        DatePickerPopWin popWin = new DatePickerPopWin.Builder(this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                Calendar calendar = (Calendar) currentDateAndTime.clone();
                calendar.set(year, month - 1, day);
                //returnText = dateDesc;
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
        popWin.showPopWin(this);


        Date date = currentDateAndTime.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(date);
        //return returnText;
    }
}
