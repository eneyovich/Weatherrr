package com.dzondza.vasya.weatherrr;


/**
 * RecyclerView item content
 */

public class WeatherParameters {
    String weatherDate;
    int imageResource;
    String temper;
    String weatherData;


    public WeatherParameters(String weatherDate, int imageResource, String temper, String weatherData) {
        this.weatherDate = weatherDate;
        this.imageResource = imageResource;
        this.temper = temper;
        this.weatherData = weatherData;
    }
}