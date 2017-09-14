package com.dzondza.vasya.weatherrr;

import com.dzondza.vasya.weatherrr.GsonStructure.FiveDaysGsonStructure;
import com.dzondza.vasya.weatherrr.GsonStructure.SixteenDaysGsonStructure;
import com.dzondza.vasya.weatherrr.GsonStructure.TodayGsonStructure;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * includes methodes for work, using retrofit2
 */

public interface OpenWeatherMapApi {

    @GET("weather")
    Call<TodayGsonStructure> getTodayWeather(@QueryMap Map<String, String> parameters);


    @GET("forecast")
    Call<FiveDaysGsonStructure> getFiveDaysWeather
            (@Query("q") String city, @Query("units") String units, @Query("APPID") String appid);


    @GET("forecast/daily")
    Call<SixteenDaysGsonStructure> getSixteenDaysWeather
            (@Query("q") String city, @Query("units") String units,
             @Query("cnt") int cnt, @Query("APPID") String appid);

}
