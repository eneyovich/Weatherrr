package com.dzondza.vasya.weatherrr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * shows forecast for 16 days
 */

public class SixteenDaysFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler_view_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY, "City not Found");
        getJSON(cityName);

        initializeRecycler(view);

        return view;
    }


    @Override
    void getJSON(final String city) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                setBasicJson(initializeBasicJson("http://api.openweathermap.org/data/2.5/forecast/daily?q=",
                        city, "&units=metric&cnt=16&APPID=419b4a7ba318ef5286319f89b37ed373"));

                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {

                try {
                    JSONArray allDaysJson = getBasicJson().getJSONArray("list");

                    for (int i = 0; i < allDaysJson.length(); i++) {

                        JSONObject oneDayJSON = allDaysJson.getJSONObject(i);

                        JSONObject tempOneDayJson = oneDayJSON.getJSONObject("temp");

                        setMinTemp(tempOneDayJson.getDouble("min"));
                        setMaxTemp(tempOneDayJson.getDouble("max"));
                        setPressure(oneDayJSON.getDouble("pressure"));
                        setHumidity(oneDayJSON.getInt("humidity"));
                        setSpeed(oneDayJSON.getDouble("speed"));
                        setClouds(oneDayJSON.getInt("clouds"));
                        setDirection(oneDayJSON.getDouble("deg"));


                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.DAY_OF_YEAR, i);
                        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd y");
                        String date = sdf.format(calendar.getTime());


                        JSONObject weatherJson = oneDayJSON.getJSONArray("weather").getJSONObject(0);
                        int imgResource = getImage(weatherJson.getString("description"));


                        mForecastRecyclerList.add(new WeatherParameters(date, imgResource, new StringBuilder()
                                .append(getMinTemp()).append("/").append(getMaxTemp()).append(" \u00B0C")
                                .toString(), weatherParamsInTextView()));

                        mRecyclerAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.loading_error), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}