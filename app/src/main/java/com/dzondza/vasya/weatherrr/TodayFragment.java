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
import org.xmlpull.v1.XmlPullParser;

import java.util.Calendar;


/**
 * shows today forecast
 */

public class TodayFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY, "City not Found");

        getDataFromXML(cityName);

        return view;
    }


    @Override
    protected void getDataFromXML(final String city) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {

                    XmlPullParser xmlParser = registerXMLParser("http://api.openweathermap.org/data/2.5/weather?q=",
                            city, "&mode=xml&units=metric&APPID=419b4a7ba318ef5286319f89b37ed373",
                            reader);

                    while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {

                        if (xmlParser.getEventType() == XmlPullParser.START_TAG) {

                            switch (xmlParser.getName()) {

                                case "temperature":
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        temp = Double.parseDouble(xmlParser.getAttributeValue(0));
                                    }

                                case "pressure":
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        pressure = Double.parseDouble(xmlParser.getAttributeValue(0));
                                    }
                                    break;

                                case "humidity":
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        humidity = Integer.parseInt(xmlParser.getAttributeValue(0));
                                    }

                                case "speed":
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        speed = Double.parseDouble(xmlParser.getAttributeValue(0));
                                    }
                                    break;

                                case "clouds":
                                    if (xmlParser.getAttributeName(1).equals("name")) {
                                        weatherList.add(xmlParser.getAttributeValue(1));
                                    }
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        clouds = Integer.parseInt(xmlParser.getAttributeValue(0));
                                    }
                                    break;

                                case "direction":
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        direction = Double.parseDouble(xmlParser.getAttributeValue(0));
                                    }
                                    break;
                            }
                        }
                        xmlParser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                setToTextView(R.id.text_view_today_time, Calendar.getInstance().getTime().toString());

                ImageView forecastImage = getView().findViewById(R.id.today_image_view);
                int imgResource = getImage(weatherList.get(0));
                forecastImage.setImageResource(imgResource);

                setToTextView(R.id.text_view_today_temp, "" + temp + " \u00B0C " + weatherList.get(0));
                setToTextView(R.id.text_view_today_pressure, "Pressure           " + pressure + " hPa");
                setToTextView(R.id.text_view_today_humidity, "Humidity          " + humidity + " %");
                setToTextView(R.id.text_view_today_speed, "Speed              " + speed + " m/s");
                setToTextView(R.id.text_view_today_direction, "Direction          " + direction + " deg");
                setToTextView(R.id.text_view_today_clouds, "Clouds             " + clouds + " %");

            }
        }.execute();
    }

    //sets value to textView
    private void setToTextView(int textViewId, String information) {
        TextView tView = getView().findViewById(textViewId);
        tView.setText(information);
    }
}