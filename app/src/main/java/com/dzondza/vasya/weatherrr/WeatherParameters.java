package com.dzondza.vasya.weatherrr;


/**
 * Created by nik on 08.08.2017.
 */

public class WeatherParameters {
    String weatherDate;
    int imageResource;
    String temper;
    String weatherData;


    WeatherParameters(String weatherDate, int imageResource, String temper, String weatherData) {
        this.weatherDate = weatherDate;
        this.imageResource = imageResource;
        this.temper = temper;
        this.weatherData = weatherData;
    }
}