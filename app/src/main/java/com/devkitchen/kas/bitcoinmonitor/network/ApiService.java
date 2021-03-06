package com.devkitchen.kas.bitcoinmonitor.network;

import com.devkitchen.kas.bitcoinmonitor.models.CurrentCoinTime;
import com.devkitchen.kas.bitcoinmonitor.models.CurrentGetCoin;
import com.devkitchen.kas.bitcoinmonitor.models.GetCoin;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * created by Kassen Dauren 29.10.2018
 */

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

    /**
     * Response for retrieving current price for chosen currency
     *
     * @param start
     * @param end
     * @param currency
     */
    @GET("currentprice/{CODE}.json")
    io.reactivex.Observable<CurrentGetCoin> getCurrentPrice(@Path("CODE") String code);
    // Default response for retrieving current data
    //@GET("currentprice.json")
    //Observable<> getCurrentData();
}
