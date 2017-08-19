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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * shows forecast for 16 days
 */

public class SixteenDaysFragment extends BaseFragment {
    private GsonStructure gsonData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY, "City not Found");
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
                    URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=" +
                            city + "&units=metric&cnt=16&APPID=419b4a7ba318ef5286319f89b37ed373");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    Gson gson = new Gson();
                    gsonData = gson.fromJson(reader, GsonStructure.class);

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

                    minTemp = gsonData.list[i].temp.min;
                    maxTemp = gsonData.list[i].temp.max;
                    pressure = gsonData.list[i].pressure;
                    humidity = gsonData.list[i].humidity;
                    speed = gsonData.list[i].speed;
                    clouds = gsonData.list[i].clouds;
                    direction = gsonData.list[i].deg;

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, i);
                    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd y");
                    String date = sdf.format(calendar.getTime());

                    descript = gsonData.list[i].weather[0].description;
                    int imgResource = getImage(descript);


                    forecastRecyclerList.add(new WeatherParameters(date, imgResource, new StringBuilder()
                            .append(minTemp).append("/").append(maxTemp).append(" \u00B0C")
                            .toString(), weatherParamsInTextView()));
                    recyclerAdapter.notifyDataSetChanged();
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