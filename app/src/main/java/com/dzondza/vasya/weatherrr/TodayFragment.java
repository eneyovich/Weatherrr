package com.dzondza.vasya.weatherrr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Calendar;


/**
 * shows today's forecast
 */

public class TodayFragment extends BaseFragment {
    private GsonStructure gsonData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY);
        if (cityName != null && !cityName.isEmpty()) {
            getJSON(cityName);
        } else {
            getJSON("London");
        }

        return view;
    }


    @Override
    protected void getJSON(final String city) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    HttpURLConnection connection = connect("http://api.openweathermap.org/data/2.5/weather?q=",
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

                mDescript = gsonData.weather[0].description;
                mTemp = gsonData.main.temp;
                mPressure = gsonData.main.pressure;
                mHumidity = gsonData.main.humidity;
                mSpeed = gsonData.wind.speed;
                mDirection = gsonData.wind.deg;
                mClouds = gsonData.clouds.all;


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
        }.execute();
    }


    //sets value to textView
    private void setToTextView(int textViewId, String information) {
        TextView tView = (TextView) getView().findViewById(textViewId);
        tView.setText(information);
    }



    //Gson structure
    private class GsonStructure {
        private Main main;
        private Wind wind;
        private Clouds clouds;
        private Weather[] weather;

        private class Main {
            private double temp;
            private double pressure;
            private int humidity;
        }

        private class Wind {
            private double speed;
            private double deg;
        }

        private class Clouds {
            private int all;
        }

        private class Weather {
            private String description;
        }
    }
}