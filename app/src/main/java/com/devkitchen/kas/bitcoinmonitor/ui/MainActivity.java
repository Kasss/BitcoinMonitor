package com.devkitchen.kas.bitcoinmonitor.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devkitchen.kas.bitcoinmonitor.R;
import com.devkitchen.kas.bitcoinmonitor.components.Utilities;
import com.devkitchen.kas.bitcoinmonitor.models.GetCoin;
import com.devkitchen.kas.datetimepicker.popwindow.DatePickerPopWin;
import com.devkitchen.kas.datetimepicker.popwindow.WheelPickerPopWin;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Kassen Dauren 25.10.2018
 */

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

    @BindView(R.id.disclaimer_text)
    TextView disclaimerText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @NonNull
    private String startDate, endDate;
    private String currency;
    private static String TAG = "MainActivity GetCoin response: ";
    private ArrayList<String> years;
    private ArrayList<String> prices;
    MainPresenter mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        years = new ArrayList<>();
        prices = new ArrayList<>();
        /* Strings required initialization since isEmpty statement on NULL object reference */
        startDate = "";
        endDate = "";
        currency = "USD";
        currencyText.setOnClickListener(this);
        startEditText.setOnClickListener(this);
        endEditText.setOnClickListener(this);
        showResult.setOnClickListener(this);
    }

    private void initBarChart () {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < prices.size(); i++)
        {
            barEntries.add(new BarEntry(Float.valueOf(prices.get(i)), i));
        }
        labels.addAll(years);
        BarDataSet barDataSet = new BarDataSet(barEntries, "Цена");
        BarData data = new BarData(labels, barDataSet);
        barChart.setData(data);
        barChart.setDescription("");
    }

    private void setupMVP() {
        mp = new MainPresenter(this, startDate, endDate, currency);
        getCoins();
    }

    private void getCoins() {
        mp.getCoin();
    }


    @Override
    public void showToast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void displayCoins(GetCoin coin) {
        hideProgressBar();
        if (coin != null) {
            disclaimerText.setText(coin.getDisclaimer());
            years.addAll(Utilities.getKeys(coin.getBpi()));
            prices.addAll(Utilities.getValues(coin.getBpi()));
            initBarChart();
            Log.d(TAG, coin.getDisclaimer());
        } else {
            Log.d(TAG, "Response null");
        }
    }

    @Override
    public void displayError(String e) {
        hideProgressBar();
        showToast(e);
    }

    @Override
    public void onClick(View view) {
        if (view == startEditText) {
            getDate(1);
        }
        if (view == endEditText) {
            getDate(2);
        }
        if (view == currencyText) {
            getCurrency();
        }
        if (view == showResult) {
            checkEvent();
        }
    }

    public void checkEvent() {
        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(MainActivity.this, "Введите дату!", Toast.LENGTH_LONG).show();
        }
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            if (Utilities.convertDate(startDate).after(Utilities.convertDate(endDate))) {
                Toast.makeText(MainActivity.this, "Начальная дата не должна превышать или быть равна!", Toast.LENGTH_LONG).show();
            } else {
                setupMVP();
                showProgressBar();
            }
        }
    }


    public void getCurrency() {
        final ArrayList<String> currencyList = new ArrayList<>();
        currencyList.add("USD");
        currencyList.add("EUR");
        currencyList.add("GBP");
        currencyList.add("KZT");

        WheelPickerPopWin pickerPopWin = new WheelPickerPopWin.Builder(this, new WheelPickerPopWin.OnWheelPickedListener() {
            @Override
            public void onWheelPickCompleted(String value) {
                currency = value;
                currencyText.setText(currency);
            }
        }).textCenterTextView("Валюта")
                .textConfirm("Готово")          //text of confirm button
                .textCancel("Отмена")           //text of cancel button
                .btnTextSize(16)            // button text size
                .viewTextSize(20)           // pick view text size
                .colorCancel(Color.parseColor("#999999"))        //color of cancel button
                .colorConfirm(Color.parseColor("#2d095c"))       //color of confirm button
                .wheelList(currencyList)
                .itemChose(0)           // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(this);
    }

    public void getDate(final int editTextNumber) {

        final Calendar currentDateAndTime = Calendar.getInstance();
        final Calendar selectedDate = (Calendar) currentDateAndTime.clone();
        final Calendar minDate = (Calendar) currentDateAndTime.clone();
        final Calendar maxDate = (Calendar) currentDateAndTime.clone();
        maxDate.set(Calendar.YEAR, currentDateAndTime.get(Calendar.YEAR));
        minDate.set(Calendar.YEAR, 2010);
        Date date = currentDateAndTime.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(date);
        DatePickerPopWin popWin = new DatePickerPopWin.Builder(this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                Calendar calendar = (Calendar) currentDateAndTime.clone();
                calendar.set(year, month - 1, day);
                //returnText = dateDesc;
                switch (editTextNumber) {
                    case 1:
                        startDate = dateDesc;
                        startEditText.setText(startDate);
                        break;
                    case 2:
                        endDate = dateDesc;
                        endEditText.setText(endDate);
                        break;
                    default:
                        startDate = dateDesc;
                        startEditText.setText(startDate);
                        break;
                }
            }
        }).textConfirm("ОК")
                .textCancel("Отмена")
                .btnTextSize(16)
                .viewTextSize(18)
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#2d095c"))
                .minYear(minDate.get(Calendar.YEAR)) //min year in loop
                .maxYear(maxDate.get(Calendar.YEAR)) // max year in loop
                .minDate(minDate)
                .maxDate(maxDate)
                .build();
        popWin.showPopWin(this);

    }
}
