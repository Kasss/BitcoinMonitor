package com.devkitchen.kas.bitcoinmonitor.ui;

import android.util.Log;

import com.devkitchen.kas.bitcoinmonitor.models.CurrentGetCoin;
import com.devkitchen.kas.bitcoinmonitor.models.GetCoin;
import com.devkitchen.kas.bitcoinmonitor.network.Api;
import com.devkitchen.kas.bitcoinmonitor.network.ApiService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;


/**
 * created by Kassen Dauren 25.10.2018
 * last update 26.10.2018
 */

public class MainPresenter implements MainPresenterInterface {

    private MainViewInterface mainViewInterface;
    private String start, end, currency;

    /**
     * Constructor for implementing retrieve response (get value in specific period)
     *
     * @author Kassen Dauren
     */

    public MainPresenter(MainViewInterface mainViewInterface, String start, String end, String currency) {
        this.mainViewInterface = mainViewInterface;
        this.start = start;
        this.end = end;
        this.currency = currency;
    }

    /**
     * Constructor for implementing current price response
     *
     * @author Kassen Dauren
     */

    public MainPresenter(MainViewInterface mainViewInterface, String currency) {
        this.mainViewInterface = mainViewInterface;
        this.currency = currency;
    }

    /**
     * Override Interface method for call subscribe (get value in specific period)
     *
     * @return none return type
     * @author Kassen Dauren
     */
    @Override
    public void getCoin() {
        getObservable().subscribeWith(getObserver());
    }


    /**
     * Override Interface method for call subscribe (current price response)
     *
     * @return none return type
     * @author Kassen Dauren
     */
    @Override
    public void getCurrentCoin() {
        getCoinObservable().subscribeWith(getCoinDisposableObserver());
    }

    /**
     * Observable for implementing Api and build Retrofit with response
     *
     * @return returns built Retrofit with response in Observable type
     * @author Kassen Dauren
     */

    public Observable<GetCoin> getObservable() {
        return Api.getRetrofit().create(ApiService.class)
                .getCoins(start, end, currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * DisposableObserver for implementing response and implementing interface methods
     *
     * @return returns built response in DisposableObserver type
     * @author Kassen Dauren
     */

    public DisposableObserver<GetCoin> getObserver() {
        return new DisposableObserver<GetCoin>() {

            @Override
            public void onNext(@NonNull GetCoin coin) {
                Log.d(TAG, "OnNext" + coin.getBpi());
                mainViewInterface.displayCoins(coin);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
                mainViewInterface.displayError("Error fetching while Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                mainViewInterface.hideProgressBar();
            }
        };
    }


    // Лень было переделать на multisubcribe, не увидел второе задание так что тупо сделано но работает

    /**
     * Observable for implementing Api and build Retrofit with response
     *
     * @return returns built Retrofit with response in Observable type
     * @author Kassen Dauren
     */
    public Observable<CurrentGetCoin> getCoinObservable() {
        return Api.getRetrofit().create(ApiService.class)
                .getCurrentPrice(currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * DisposableObserver for implementing response and implementing interface methods
     *
     * @return returns built response in DisposableObserver type
     * @author Kassen Dauren
     */
    public DisposableObserver<CurrentGetCoin> getCoinDisposableObserver() {
        return new DisposableObserver<CurrentGetCoin>() {

            @Override
            public void onNext(@NonNull CurrentGetCoin coin) {
                Log.d(TAG, "OnNext" + coin.getBpi());
                mainViewInterface.displayCurrentCoin(coin);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "Error" + e);
                e.printStackTrace();
                mainViewInterface.displayError("Error fetching while Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Completed");
                mainViewInterface.hideProgressBar();
            }
        };
    }

}
