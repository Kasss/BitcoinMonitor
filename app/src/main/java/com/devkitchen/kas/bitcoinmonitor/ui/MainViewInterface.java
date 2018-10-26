package com.devkitchen.kas.bitcoinmonitor.ui;

import com.devkitchen.kas.bitcoinmonitor.models.CurrentGetCoin;
import com.devkitchen.kas.bitcoinmonitor.models.GetCoin;

/**
 * created by Kassen Dauren 25.10.2018
 * last update 26.10.2018
 */

/**
 * Interface for inner call
 *
 * @author Kassen Dauren
 * @return none return type
 */
public interface MainViewInterface {
    void showToast(String s);

    void showProgressBar();

    void hideProgressBar();

    void displayCoins(GetCoin coin);

    void displayCurrentCoin(CurrentGetCoin coin);

    void displayError(String s);
}
