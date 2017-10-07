package com.dzondza.vasya.weatherrr;


/**
 * RecyclerView Item content
 */

class WeatherParameters {
    private String weatherDate;
    private int imageResource;
    private String temper;
    private String weatherData;


    WeatherParameters(String weatherDate, int imageResource, String temper, String weatherData) {
        this.weatherDate = weatherDate;
        this.imageResource = imageResource;
        this.temper = temper;
        this.weatherData = weatherData;
    }


    public String getWeatherDate() {
        return weatherDate;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTemper() {
        return temper;
    }

    public String getWeatherData() {
        return weatherData;
    }
}