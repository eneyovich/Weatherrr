package com.dzondza.vasya.weatherrr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * shows forecast for 16 days
 */

public class SixteenDaysFragment extends BaseFragment {
    private GsonStructure gsonData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler_view_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY);
        if (cityName != null && !cityName.isEmpty()) {
            getJSON(cityName);
        } else {
            getJSON("London");
        }

        initializeRecycler(view);

        return view;
    }


    @Override
    protected void getJSON(final String city) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    HttpURLConnection connection = connect("http://api.openweathermap.org/data/2.5/forecast/daily?q=",
                            city, "&units=metric&cnt=16&APPID=419b4a7ba318ef5286319f89b37ed373");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    gsonData = new Gson().fromJson(reader, GsonStructure.class);

                    reader.close();
                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {

                for (int i = 0; i < gsonData.list.length; i++) {

                    mMinTemp = gsonData.list[i].temp.min;
                    mMaxTemp = gsonData.list[i].temp.max;
                    mPressure = gsonData.list[i].pressure;
                    mHumidity = gsonData.list[i].humidity;
                    mSpeed = gsonData.list[i].speed;
                    mClouds = gsonData.list[i].clouds;
                    mDirection = gsonData.list[i].deg;

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, i);
                    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd y");
                    String date = sdf.format(calendar.getTime());

                    mDescript = gsonData.list[i].weather[0].description;
                    int imgResource = getImage(mDescript);


                    mForecastRecyclerList.add(new WeatherParameters(date, imgResource, new StringBuilder()
                            .append(mMinTemp).append("/").append(mMaxTemp).append(" \u00B0C")
                            .toString(), weatherParamsInTextView()));
                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }


    //Gson structure
    private class GsonStructure {

        private List[] list;

        private class List {
            private double pressure;
            private int humidity;
            private double speed;
            private int clouds;
            private double deg;
            private Temp temp;
            private Weather[] weather;

            private class Temp {
                private double min;
                private double max;
            }

            private class Weather {
                private String description;
            }
        }
    }
}