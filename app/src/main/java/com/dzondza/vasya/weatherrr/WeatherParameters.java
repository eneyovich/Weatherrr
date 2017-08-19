package com.dzondza.vasya.weatherrr;


/**
 * parameters in recyclerView item
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