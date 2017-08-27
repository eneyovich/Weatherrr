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
import java.net.URL;
import java.util.Calendar;


/**
 * shows today's forecast
 */

public class TodayFragment extends BaseFragment {
    private GsonStructure gsonData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY, "City not Found");
        getJSON(cityName);

        return view;
    }


    @Override
    void getJSON(final String city) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + city +
                            "&units=metric&APPID=419b4a7ba318ef5286319f89b37ed373");
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

                setDescript(gsonData.weather[0].description);
                setTemp(gsonData.main.temp);
                setPressure(gsonData.main.pressure);
                setHumidity(gsonData.main.humidity);
                setSpeed(gsonData.wind.speed);
                setDirection(gsonData.wind.deg);
                setClouds(gsonData.clouds.all);


                setToTextView(R.id.text_view_today_time, Calendar.getInstance().getTime().toString());

                ImageView forecastImage = getView().findViewById(R.id.today_image_view);
                int imgResource = getImage(getDescript());
                forecastImage.setImageResource(imgResource);

                setToTextView(R.id.text_view_today_temp, "" + getTemp() + " \u00B0C " + getDescript());
                setToTextView(R.id.text_view_today_pressure, "Pressure           " + getPressure() + " hPa");
                setToTextView(R.id.text_view_today_humidity, "Humidity          " + getHumidity() + " %");
                setToTextView(R.id.text_view_today_speed, "Speed              " + getSpeed() + " m/s");
                setToTextView(R.id.text_view_today_direction, "Direction          " + getDirection() + " deg");
                setToTextView(R.id.text_view_today_clouds, "Clouds             " + getClouds() + " %");
            }
        }.execute();
    }


    //sets value to textView
    private void setToTextView(int textViewId, String information) {
        TextView tView = getView().findViewById(textViewId);
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