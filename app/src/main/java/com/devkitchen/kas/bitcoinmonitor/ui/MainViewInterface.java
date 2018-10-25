package com.devkitchen.kas.bitcoinmonitor.ui;

import com.devkitchen.kas.bitcoinmonitor.models.GetCoin;

public interface MainViewInterface {
    void showToast(String s);
    void showProgressBar();
    void hideProgressBar();
    void displayCoins(GetCoin coin);
    void displayError(String s);
}
