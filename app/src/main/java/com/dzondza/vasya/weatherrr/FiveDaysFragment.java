package com.dzondza.vasya.weatherrr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;


/**
 * shows forecast for 5 days in every 3 hours
 */

public class FiveDaysFragment extends BaseFragment {

    @Nullable
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

                setBasicJson(initializeBasicJson("http://api.openweathermap.org/data/2.5/forecast?q=",
                        city, "&units=metric&APPID=419b4a7ba318ef5286319f89b37ed373"));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    JSONArray allDaysJson = getBasicJson().getJSONArray("list");
                    for (int i = 0; i < allDaysJson.length(); i++) {
                        JSONObject oneDayJSON = allDaysJson.getJSONObject(i);

                        JSONObject mainJson = oneDayJSON.getJSONObject("main");
                        setTemp(mainJson.getDouble("temp"));
                        setPressure(mainJson.getDouble("pressure"));
                        setHumidity(mainJson.getInt("humidity"));

                        JSONObject windJson = oneDayJSON.getJSONObject("wind");
                        setDirection(windJson.getDouble("deg"));
                        setSpeed(windJson.getDouble("speed"));
                        setClouds(oneDayJSON.getJSONObject("clouds").getInt("all"));

                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.HOUR_OF_DAY, i*3);
                        Date time = calendar.getTime();


                        JSONObject weatherJson = oneDayJSON.getJSONArray("weather").getJSONObject(0);
                        int imgResource = getImage(weatherJson.getString("description"));


                        forecastRecyclerList.add(new WeatherParameters(time.toString(), imgResource,
                                getTemp() + " \u00B0C", weatherParamsInTextView()));

                        recyclerAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.loading_error), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}