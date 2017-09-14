package com.dzondza.vasya.weatherrr;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * basic fragment for today, 5- and 16- days fragments
 */

public abstract class BaseFragment extends Fragment {

    protected int mHumidity, mClouds;
    protected double mTemp, mMinTemp, mMaxTemp, mPressure, mSpeed, mDirection;
    protected String mDescript;
    protected RecyclerAdapter mRecyclerAdapter;
    protected List<WeatherParameters> mForecastRecyclerList;


    protected void initializeRecycler (View view){
        mForecastRecyclerList = new ArrayList<>();

        RecyclerView mRecyclerView = view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        mRecyclerAdapter = new RecyclerAdapter(mForecastRecyclerList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                manager.getOrientation()));

        mRecyclerView.setHasFixedSize(true);
    }


    //sets weather params in texView in 5 & 16 days fragments
    protected String weatherParamsInTextView() {
        return "Pressure " + mPressure + " hPa\nHumidity " + mHumidity + " %\nSpeed " + mSpeed
                + " m/s\nClouds " + mClouds + " %\nDirection " + mDirection + " deg";
    }


    //returns image resource id according to weather description
    protected int getImage(String weatherDescript) {
        int imageResource;
        switch (weatherDescript) {
            case "light rain":
            case "light intensity shower rain":
                imageResource = R.drawable.drawable_raining_little;
                break;
            case "overcast clouds":
            case "haze":
                imageResource = R.drawable.drawable_foggy_much;
                break;
            case "moderate rain":
            case "heavy intensity rain":
            case "shower rain":
                imageResource = R.drawable.drawable_raining_medium;
                break;
            case "broken clouds":
                imageResource = R.drawable.drawable_foggy_medium;
                break;
            case "scattered clouds":
            case "few clouds":
                imageResource = R.drawable.drawable_foggy_minimum;
                break;
            case "clear sky":
            case "sky is clear":
                imageResource = R.drawable.drawable_sunny;
                break;
            default:
                imageResource = R.drawable.drawable_unknown;
        }
        return imageResource;
    }


    protected HttpURLConnection connect(String urlBegin, String city, String urlEnd) {
        try {
            URL url = new URL(urlBegin + city + urlEnd);
            return (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected OpenWeatherMapApi getRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(OpenWeatherMapApi.class);
    }


    protected abstract void getJSON(final String city);
}