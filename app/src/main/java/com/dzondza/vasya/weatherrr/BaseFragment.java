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

    int humidity, clouds;
    double temp, minTemp, maxTemp, pressure, speed, direction;
    String descript;
    RecyclerAdapter recyclerAdapter;
    protected List<WeatherParameters> forecastRecyclerList;


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
}