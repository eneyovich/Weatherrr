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
 * shows today's forecast
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

                mBasicJson = initializeBasicJson("http://api.openweathermap.org/data/2.5/weather?q=",
                        city, "&units=metric&APPID=419b4a7ba318ef5286319f89b37ed373");

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    JSONObject mainJson = mBasicJson.getJSONObject("main");
                    JSONObject windJson = mBasicJson.getJSONObject("wind");
                    JSONObject weatherJson = mBasicJson.getJSONArray("weather").getJSONObject(0);

                    mTemp = mainJson.getDouble("temp");
                    mPressure = mainJson.getDouble("pressure");
                    mHumidity = mainJson.getInt("humidity");
                    mSpeed = windJson.getDouble("speed");
                    mDirection = windJson.getDouble("deg");
                    mClouds = mBasicJson.getJSONObject("clouds").getInt("all");
                    String weatherDescript = weatherJson.getString("description");


                    setToTextView(R.id.text_today_time, Calendar.getInstance().getTime().toString());

                    ImageView forecastImage = (ImageView) getView().findViewById(R.id.image_today);
                    int imgResource = getImage(weatherDescript);
                    forecastImage.setImageResource(imgResource);

                    setToTextView(R.id.text_today_temp, "" + mTemp + " \u00B0C " + weatherDescript);
                    setToTextView(R.id.text_today_pressure, "Pressure           " + mPressure + " hPa");
                    setToTextView(R.id.text_today_humidity, "Humidity          " + mHumidity + " %");
                    setToTextView(R.id.text_today_speed, "Speed              " + mSpeed + " m/s");
                    setToTextView(R.id.text_today_direction, "Direction          " + mDirection + " deg");
                    setToTextView(R.id.text_today_clouds, "Clouds             " + mClouds + " %");

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), getString(R.string.loading_error), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    //sets value to textView
    private void setToTextView(int textViewId, String information) {
        TextView tView = (TextView) getView().findViewById(textViewId);
        tView.setText(information);
    }
}