package com.devkitchen.kas.bitcoinmonitor.ui;

import android.util.Log;

import com.devkitchen.kas.bitcoinmonitor.models.GetCoin;
import com.devkitchen.kas.bitcoinmonitor.network.Api;
import com.devkitchen.kas.bitcoinmonitor.network.ApiService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class MainPresenter implements MainPresenterInterface {

    private MainViewInterface mainViewInterface;
    private String start, end, currency;
    public MainPresenter(MainViewInterface mainViewInterface, String start, String end, String currency) {
        this.mainViewInterface = mainViewInterface;
        this.start = start;
        this.end = end;
        this.currency = currency;
    }

    @Override
    public void getCoin() {
        getObservable().subscribeWith(getObserver());
    }

    public Observable<GetCoin> getObservable(){
        return Api.getRetrofit().create(ApiService.class)
                .getCoins(start, end, currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<GetCoin> getObserver(){
        return new DisposableObserver<GetCoin>() {

            @Override
            public void onNext(@NonNull GetCoin coin) {
                Log.d(TAG,"OnNext" + coin.getBpi());
                mainViewInterface.displayCoins(coin);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG,"Error"+e);
                e.printStackTrace();
                mainViewInterface.displayError("Error fetching while Data");
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"Completed");
                mainViewInterface.hideProgressBar();
            }
        };
    }
}
