package com.devkitchen.kas.bitcoinmonitor.network;

import com.devkitchen.kas.bitcoinmonitor.models.GetCoin;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * Response for the selected (by the user) currency, for the specified time interval (by the user)
     *
     * @param start
     * @param end
     * @param currency
     */
    @GET("historical/close.json?")
    io.reactivex.Observable<GetCoin> getCoins(@Query("start") String start, @Query("end") String end, @Query("currency") String currency);

    // Default response for retrieving current data
    //@GET("currentprice.json")
    //Observable<> getCurrentData();
}
