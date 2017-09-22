package com.dzondza.vasya.weatherrr;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dzondza.vasya.weatherrr.GsonStructure.FiveDaysGsonStructure;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * shows forecast for 5 days in every 3 hours
 */

public class FiveDaysFragment extends BaseFragment {

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

        Call<FiveDaysGsonStructure> gsonStructureCall = null;
        if (city == null || city.isEmpty()) {
            gsonStructureCall = weatherMapApi.getFiveDaysWeather("London", "metric", "419b4a7ba318ef5286319f89b37ed373");
        } else {
            gsonStructureCall = weatherMapApi.getFiveDaysWeather(city, "metric", "419b4a7ba318ef5286319f89b37ed373");
        }

        gsonStructureCall.enqueue(new Callback<FiveDaysGsonStructure>() {
            @Override
            public void onResponse(@NonNull Call<FiveDaysGsonStructure> call,
                                   @NonNull Response<FiveDaysGsonStructure> response) {
                for (int i = 0; i < response.body().getList().length; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.HOUR_OF_DAY, i*3);
                    Date time = calendar.getTime();

                    mTemp = response.body().getList()[i].main.getTemp();
                    mPressure = response.body().getList()[i].main.getPressure();
                    mHumidity = response.body().getList()[i].main.getHumidity();
                    mDirection = response.body().getList()[i].wind.getDeg();
                    mSpeed = response.body().getList()[i].wind.getSpeed();
                    mClouds = response.body().getList()[i].clouds.getAll();

                    mDescript = response.body().getList()[i].weather[0].getDescription();
                    int imgResource = getImage(mDescript);


                    mForecastRecyclerList.add(new WeatherParameters(time.toString(), imgResource,
                            mTemp + " \u00B0C", weatherParamsInTextView()));
                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FiveDaysGsonStructure> call, Throwable t) {
                Log.v("onFailure", call.request().toString() + "Throwable: " + t);
                Toast.makeText(getActivity(), "Data not loaded/found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}