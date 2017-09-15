package com.dzondza.vasya.weatherrr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dzondza.vasya.weatherrr.GsonStructure.TodayGsonStructure;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * shows today's forecast
 */

public class TodayFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY);
        getJSON(cityName);

        return view;
    }

    //sets value to textView
    private void setToTextView(int textViewId, String information) {
        TextView tView = (TextView) getView().findViewById(textViewId);
        tView.setText(information);
    }


    @Override
    protected void getJSON(String city) {

        OpenWeatherMapApi weatherMapApi = getRetrofit();

        Map<String, String> parameters = new HashMap<>();
        if (city == null || city.isEmpty()) {
            parameters.put("q", "London");
        } else {
            parameters.put("q", city);
        }
        parameters.put("units", "metric");
        parameters.put("APPID", "419b4a7ba318ef5286319f89b37ed373");

        Call<TodayGsonStructure> gsonStructureCall = weatherMapApi.getTodayWeather(parameters);

        gsonStructureCall.enqueue(new Callback<TodayGsonStructure>() {
            @Override
            public void onResponse(Call<TodayGsonStructure> call, Response<TodayGsonStructure> response) {

                if (response.body() != null) {
                    mDescript = response.body().getWeather()[0].getDescription();
                    mTemp = response.body().getMain().getTemp();
                    mPressure = response.body().getMain().getPressure();
                    mHumidity = response.body().getMain().getHumidity();
                    mSpeed = response.body().getWind().getSpeed();
                    mDirection = response.body().getWind().getDeg();
                    mClouds = response.body().getClouds().getAll();
                } else {
                    Toast.makeText(getActivity(), "Data not loaded/found", Toast.LENGTH_SHORT).show();
                }

                setToTextView(R.id.text_today_time, Calendar.getInstance().getTime().toString());

                ImageView forecastImage = (ImageView) getView().findViewById(R.id.image_today);
                int imgResource = getImage(mDescript);
                forecastImage.setImageResource(imgResource);

                setToTextView(R.id.text_today_temp, "" + mTemp + " \u00B0C " + mDescript);
                setToTextView(R.id.text_today_pressure, "Pressure           " + mPressure + " hPa");
                setToTextView(R.id.text_today_humidity, "Humidity          " + mHumidity + " %");
                setToTextView(R.id.text_today_speed, "Speed              " + mSpeed + " m/s");
                setToTextView(R.id.text_today_direction, "Direction          " + mDirection + " deg");
                setToTextView(R.id.text_today_clouds, "Clouds             " + mClouds + " %");
            }

            @Override
            public void onFailure(Call<TodayGsonStructure> call, Throwable t) {
                Log.v("onFailure", call.request().toString() + "Throwable: " + t);
                Toast.makeText(getActivity(), "Data not loaded/found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}