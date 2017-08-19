package com.dzondza.vasya.weatherrr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;


/**
 * shows today forecast
 */

public class TodayFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY, "City not Found");
        getJSON(cityName);

        return view;
    }


    @Override
    protected void getJSON(final String city) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                setBasicJson(initializeBasicJson("http://api.openweathermap.org/data/2.5/weather?q=",
                        city, "&units=metric&APPID=419b4a7ba318ef5286319f89b37ed373"));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    JSONObject mainJson = getBasicJson().getJSONObject("main");
                    JSONObject windJson = getBasicJson().getJSONObject("wind");
                    JSONObject weatherJson = getBasicJson().getJSONArray("weather").getJSONObject(0);

                    setTemp(mainJson.getDouble("temp"));
                    setPressure(mainJson.getDouble("pressure"));
                    setHumidity(mainJson.getInt("humidity"));
                    setSpeed(windJson.getDouble("speed"));
                    setDirection(windJson.getDouble("deg"));
                    setClouds(getBasicJson().getJSONObject("clouds").getInt("all"));
                    String weatherDescript = weatherJson.getString("description");


                    setToTextView(R.id.text_view_today_time, Calendar.getInstance().getTime().toString());

                    ImageView forecastImage = getView().findViewById(R.id.today_image_view);
                    int imgResource = getImage(weatherDescript);
                    forecastImage.setImageResource(imgResource);

                    setToTextView(R.id.text_view_today_temp, "" + getTemp() + " \u00B0C " + weatherDescript);
                    setToTextView(R.id.text_view_today_pressure, "Pressure           " + getPressure() + " hPa");
                    setToTextView(R.id.text_view_today_humidity, "Humidity          " + getHumidity() + " %");
                    setToTextView(R.id.text_view_today_speed, "Speed              " + getSpeed() + " m/s");
                    setToTextView(R.id.text_view_today_direction, "Direction          " + getDirection() + " deg");
                    setToTextView(R.id.text_view_today_clouds, "Clouds             " + getClouds() + " %");

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), getString(R.string.loading_error), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    //sets value to textView
    private void setToTextView(int textViewId, String information) {
        TextView tView = getView().findViewById(textViewId);
        tView.setText(information);
    }
}