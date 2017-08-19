package com.dzondza.vasya.weatherrr;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;


/**
 * basic fragment for today, 5- and 16- days fragments
 */

public abstract class BaseFragment extends Fragment {

    private int humidity, clouds;
    private double temp, minTemp, maxTemp, pressure, speed, direction;
    private String descript;
    RecyclerAdapter recyclerAdapter;
    List<WeatherParameters> forecastRecyclerList;


    protected void initializeRecycler (View view){
        forecastRecyclerList = new ArrayList<>();

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_id);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        recyclerAdapter = new RecyclerAdapter(forecastRecyclerList);
        mRecyclerView.setAdapter(recyclerAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                manager.getOrientation()));

        mRecyclerView.setHasFixedSize(true);
    }


    //sets weather params in texView in 5 & 16 days fragments
    protected String weatherParamsInTextView() {
        return "pressure " + pressure + " hPa\nhumidity " + humidity + " %\nspeed " + speed
                + " m/s\nclouds " + clouds + " %\ndirection " + direction + " deg";
    }


    //chooses image resource id to descript forecast
    protected int getImage(String weatherDescript) {
        int imageResource;
        switch (weatherDescript) {
            case "light rain":
            case "light intensity shower rain":
                imageResource = R.drawable.raining_little;
                break;
            case "overcast clouds":
            case "haze":
                imageResource = R.drawable.foggy_much;
                break;
            case "moderate rain":
            case "heavy intensity rain":
            case "shower rain":
                imageResource = R.drawable.raining_medium;
                break;
            case "broken clouds":
                imageResource = R.drawable.foggy_medium;
                break;
            case "scattered clouds":
            case "few clouds":
                imageResource = R.drawable.foggy_minimum;
                break;
            case "clear sky":
            case "sky is clear":
                imageResource = R.drawable.sunny;
                break;
            default:
                imageResource = R.drawable.unknown;
        }
        return imageResource;
    }


    protected abstract void getJSON(final String city);


    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}