package com.dzondza.vasya.weatherrr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;


/**
 * shows today forecast
 */

public class TodayFragment extends BaseFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY, "City not Found");

        if (!cityName.isEmpty() && cityName != null) {
            getDataFromXML(cityName);
        } else {
            getDataFromXML("London");
        }

        return view;
    }



    @Override
    protected void getDataFromXML(final String city) {
        new Thread(() -> {
            try {
                XmlPullParser xmlParser = registerXMLParser("http://api.openweathermap.org/data/2.5/weather?q=",
                        city, "&mode=xml&units=metric&APPID=419b4a7ba318ef5286319f89b37ed373");

                while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {

                    if (xmlParser.getEventType() == XmlPullParser.START_TAG) {

                        switch (xmlParser.getName()) {

                            case "temperature":
                                if (xmlParser.getAttributeName(0).equals("value")) {
                                    mTemp = Double.parseDouble(xmlParser.getAttributeValue(0));
                                }
                                break;
                            case "pressure":
                                if (xmlParser.getAttributeName(0).equals("value")) {
                                    mPressure = Double.parseDouble(xmlParser.getAttributeValue(0));
                                }
                                break;
                            case "humidity":
                                if (xmlParser.getAttributeName(0).equals("value")) {
                                    mHumidity = Integer.parseInt(xmlParser.getAttributeValue(0));
                                }
                                break;
                            case "speed":
                                if (xmlParser.getAttributeName(0).equals("value")) {
                                    mSpeed = Double.parseDouble(xmlParser.getAttributeValue(0));
                                }
                                break;
                            case "clouds":
                                if (xmlParser.getAttributeName(1).equals("name")) {
                                    mWeatherList.add(xmlParser.getAttributeValue(1));
                                }
                                if (xmlParser.getAttributeName(0).equals("value")) {
                                    mClouds = Integer.parseInt(xmlParser.getAttributeValue(0));
                                }
                                break;
                            case "direction":
                                if (xmlParser.getAttributeName(0).equals("value")) {
                                    mDirection = Double.parseDouble(xmlParser.getAttributeValue(0));
                                }
                                break;
                        }
                    }
                    xmlParser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }

            getActivity().runOnUiThread(() -> {
                try {
                    setToTextView(R.id.text_today_time, Calendar.getInstance().getTime().toString());

                    ImageView forecastImage = (ImageView) getView().findViewById(R.id.image_today);
                    int imgResource = getImage(mWeatherList.get(0));
                    forecastImage.setImageResource(imgResource);

                    setToTextView(R.id.text_today_temp, "" + mTemp + " \u00B0C " + mWeatherList.get(0));
                    setToTextView(R.id.text_today_pressure, "Pressure           " + mPressure + " hPa");
                    setToTextView(R.id.text_today_humidity, "Humidity          " + mHumidity + " %");
                    setToTextView(R.id.text_today_speed, "Speed              " + mSpeed + " m/s");
                    setToTextView(R.id.text_today_direction, "Direction          " + mDirection + " deg");
                    setToTextView(R.id.text_today_clouds, "Clouds             " + mClouds + " %");
                }catch (Exception e) {
                    Toast.makeText(getActivity(), getString(R.string.loading_error), Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }


    //sets value to textView
    private void setToTextView(int textViewId, String information) {
        TextView tView = (TextView) getView().findViewById(textViewId);
        tView.setText(information);
    }
}