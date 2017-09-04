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
import java.util.Calendar;
import java.util.Date;


/**
 * shows forecast for 5 days in every 3 hours
 */

public class FiveDaysFragment extends BaseFragment {
    private GsonStructure gsonData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler_view_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY);
        getJSON(cityName);

        initializeRecycler(view);

        return view;
    }


    @Override
    protected void getJSON(final String city) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    HttpURLConnection connection = connect("http://api.openweathermap.org/data/2.5/forecast?q=",
                            city, "&units=metric&APPID=419b4a7ba318ef5286319f89b37ed373");
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

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.HOUR_OF_DAY, i*3);
                    Date time = calendar.getTime();

                    mTemp = gsonData.list[i].main.temp;
                    mPressure = gsonData.list[i].main.pressure;
                    mHumidity = gsonData.list[i].main.humidity;
                    mDirection = gsonData.list[i].wind.deg;
                    mSpeed = gsonData.list[i].wind.speed;
                    mClouds = gsonData.list[i].clouds.all;

                    mDescript = gsonData.list[i].weather[0].description;
                    int imgResource = getImage(mDescript);


                    mForecastRecyclerList.add(new WeatherParameters(time.toString(), imgResource,
                            mTemp + " \u00B0C", weatherParamsInTextView()));
                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }


    //Gson structure
    private class GsonStructure {

        private List[] list;

        private class List {
            private Main main;
            private Wind wind;
            private Clouds clouds;
            private Weather[] weather;

            private class Main {
                private double temp;
                private double pressure;
                private int humidity;
            }

            private class Weather {
                private String description;
            }

            private class Clouds {
                private int all;
            }

            private class Wind {
                private double speed;
                private double deg;
            }
        }
    }
}