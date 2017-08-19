package com.dzondza.vasya.weatherrr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.xmlpull.v1.XmlPullParser;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * shows forecast for 5 days in every 3 hours
 */

public class FiveDaysFragment extends BaseFragment {

    private List<Double> tempList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_view_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY, "City not Found");

        getDataFromXML(cityName);

        initializeRecycler(view);

        return view;
    }


    @Override
    protected void getDataFromXML(final String city) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    XmlPullParser xmlParser = registerXMLParser("http://api.openweathermap.org/data/2.5/forecast?q=",
                            city, "&mode=xml&units=metric&APPID=419b4a7ba318ef5286319f89b37ed373");

                    while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {

                        if (xmlParser.getEventType() == XmlPullParser.START_TAG) {

                            switch (xmlParser.getName()) {

                                case "temperature":
                                    if (xmlParser.getAttributeName(1).equals("value")) {
                                        tempList.add(Double.parseDouble(xmlParser.getAttributeValue(1)));
                                    }

                                case "pressure":
                                    if (xmlParser.getAttributeName(1).equals("value")) {
                                        pressureList.add(Double.parseDouble(xmlParser.getAttributeValue(1)));
                                    }
                                    break;

                                case "humidity":
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        humidityList.add(Integer.parseInt(xmlParser.getAttributeValue(0)));
                                    }

                                case "windSpeed":
                                    if (xmlParser.getAttributeName(0).equals("mps")) {
                                        speedList.add(Double.parseDouble(xmlParser.getAttributeValue(0)));
                                    }
                                    break;

                                case "clouds":
                                    if (xmlParser.getAttributeName(1).equals("all")) {
                                        cloudsList.add(Integer.parseInt(xmlParser.getAttributeValue(1)));
                                    }
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        weatherList.add(xmlParser.getAttributeValue(0));
                                    }
                                    break;

                                case "windDirection":
                                    if (xmlParser.getAttributeName(0).equals("deg")) {
                                        directionList.add(Double.parseDouble(xmlParser.getAttributeValue(0)));
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

                for (int i = 0; i < weatherList.size(); i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.HOUR_OF_DAY, i*3);
                    Date time = calendar.getTime();

                    int imgResource = getImage(weatherList.get(i));
                    setPressure(pressureList.get(i));
                    setHumidity(humidityList.get(i));
                    setSpeed(speedList.get(i));
                    setClouds(cloudsList.get(i));
                    setDirection(directionList.get(i));

                    forecastRecyclerList.add(new WeatherParameters(time.toString(), imgResource,
                            tempList.get(i) + " \u00B0C", weatherParamsInTextView()));

                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }
}