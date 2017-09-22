package com.dzondza.vasya.weatherrr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dzondza.vasya.weatherrr.GsonStructure.SixteenDaysGsonStructure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * shows forecast for 16 days
 */

public class SixteenDaysFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler_view_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY);
        getJSON(cityName);

        initializeRecycler(view);

        return view;
    }


    @Override
    protected void getJSON(String city) {

        OpenWeatherMapApi weatherMapApi = getRetrofit();

        Call<SixteenDaysGsonStructure> gsonStructureCall = null;
        if (city == null || city.isEmpty()) {
            gsonStructureCall = weatherMapApi.getSixteenDaysWeather("London", "metric", 16, "419b4a7ba318ef5286319f89b37ed373");
        } else {
            gsonStructureCall = weatherMapApi.getSixteenDaysWeather(city, "metric", 16, "419b4a7ba318ef5286319f89b37ed373");
        }

        gsonStructureCall.enqueue(new Callback<SixteenDaysGsonStructure>() {
            @Override
            public void onResponse(@NonNull Call<SixteenDaysGsonStructure> call,
                                   @NonNull Response<SixteenDaysGsonStructure> response) {

                List<SixteenDaysGsonStructure.WeatherList> responseList = response.body().getList();
                for (int i = 0; i < responseList.size(); i++) {

                    mMinTemp = responseList.get(i).temp.getMin();
                    mMaxTemp = responseList.get(i).temp.getMax();
                    mPressure = responseList.get(i).getPressure();
                    mHumidity = responseList.get(i).getHumidity();
                    mSpeed = responseList.get(i).getSpeed();
                    mClouds = responseList.get(i).getClouds();
                    mDirection = responseList.get(i).getDeg();

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, i);
                    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd y");
                    String date = sdf.format(calendar.getTime());

                    mDescript = responseList.get(i).weather[0].getDescription();
                    int imgResource = getImage(mDescript);


                    mForecastRecyclerList.add(new WeatherParameters(date, imgResource, new StringBuilder()
                            .append(mMinTemp).append("/").append(mMaxTemp).append(" \u00B0C")
                            .toString(), weatherParamsInTextView()));
                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SixteenDaysGsonStructure> call, Throwable t) {
                Log.v("onFailure", call.request().toString() + "Throwable: " + t);
                Toast.makeText(getActivity(), "Data not loaded/found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}