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

    private int mHumidity, mClouds;
    private double mTemp, mMinTemp, mMaxTemp, mPressure, mSpeed, mDirection;
    private String mDescript;
    RecyclerAdapter mRecyclerAdapter;
    List<WeatherParameters> mForecastRecyclerList;


    void initializeRecycler (View view){
        mForecastRecyclerList = new ArrayList<>();

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_id);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        mRecyclerAdapter = new RecyclerAdapter(mForecastRecyclerList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                manager.getOrientation()));

        mRecyclerView.setHasFixedSize(true);
    }


    //sets weather params in texView in 5 & 16 days fragments
    String weatherParamsInTextView() {
        return "mPressure " + mPressure + " hPa\nmHumidity " + mHumidity + " %\nmSpeed " + mSpeed
                + " m/s\nmClouds " + mClouds + " %\nmDirection " + mDirection + " deg";
    }


    //returns image resource id according to weather description
    int getImage(String weatherDescript) {
        int imageResource;
        switch (weatherDescript) {
            case "light rain":
            case "light intensity shower rain":
                imageResource = R.drawable.drawable_raining_little;
                break;
            case "overcast mClouds":
            case "haze":
                imageResource = R.drawable.drawable_foggy_much;
                break;
            case "moderate rain":
            case "heavy intensity rain":
            case "shower rain":
                imageResource = R.drawable.drawable_raining_medium;
                break;
            case "broken mClouds":
                imageResource = R.drawable.drawable_foggy_medium;
                break;
            case "scattered mClouds":
            case "few mClouds":
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


    abstract void getJSON(final String city);


    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        this.mHumidity = humidity;
    }

    public int getClouds() {
        return mClouds;
    }

    public void setClouds(int clouds) {
        this.mClouds = clouds;
    }

    public double getTemp() {
        return mTemp;
    }

    public void setTemp(double temp) {
        this.mTemp = temp;
    }

    public double getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(double minTemp) {
        this.mMinTemp = minTemp;
    }

    public double getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.mMaxTemp = maxTemp;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setPressure(double pressure) {
        this.mPressure = pressure;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public double getDirection() {
        return mDirection;
    }

    public void setDirection(double direction) {
        this.mDirection = direction;
    }

    public String getDescript() {
        return mDescript;
    }

    public void setDescript(String descript) {
        this.mDescript = descript;
    }
}