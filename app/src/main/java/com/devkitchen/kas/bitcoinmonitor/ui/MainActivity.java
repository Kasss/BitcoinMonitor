package com.devkitchen.kas.bitcoinmonitor.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.devkitchen.kas.bitcoinmonitor.R;
import com.devkitchen.kas.bitcoinmonitor.models.GetCoin;

public class MainActivity extends AppCompatActivity implements MainViewInterface, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void showToast(String s) {

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
    public void displayError(String s) {

    }

    @Override
    public void onClick(View view) {

    }
}
