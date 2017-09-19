package com.dzondza.vasya.weatherrr;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.xmlpull.v1.XmlPullParser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * shows forecast for 16 days
 */

public class SixteenDaysFragment extends BaseFragment {

    private List<String> mMinTempList = new ArrayList<>();
    private List<String> mMaxTempList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_recycler_view_layout, container, false);

        String cityName = getArguments().getString(MainActivity.CITY_DIALOG_KEY, "City not Found");

        if (!cityName.isEmpty() && cityName != null) {
            getDataFromXML(cityName);
        } else {
            getDataFromXML("London");
        }

        initializeRecycler(view);

        return view;
    }


    @Override
    protected void getDataFromXML(final String city) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    XmlPullParser xmlParser = registerXMLParser("http://api.openweathermap.org/data/2.5/forecast/daily?q=",
                            city, "&mode=xml&units=metric&cnt=16&APPID=419b4a7ba318ef5286319f89b37ed373");

                    while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {

                        if (xmlParser.getEventType() == XmlPullParser.START_TAG) {

                            switch (xmlParser.getName()) {

                                case "temperature":
                                    if (xmlParser.getAttributeName(1).equals("min")) {
                                        mMinTempList.add(xmlParser.getAttributeValue(1));
                                    }
                                    if (xmlParser.getAttributeName(2).equals("max")) {
                                        mMaxTempList.add(xmlParser.getAttributeValue(2));
                                    }
                                    break;
                                case "pressure":
                                    if (xmlParser.getAttributeName(1).equals("value")) {
                                        mPressureList.add(Double.parseDouble(xmlParser.getAttributeValue(1)));
                                    }
                                    break;
                                case "humidity":
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        mHumidityList.add(Integer.parseInt(xmlParser.getAttributeValue(0)));
                                    }
                                    break;
                                case "windSpeed":
                                    if (xmlParser.getAttributeName(0).equals("mps")) {
                                        mSpeedList.add(Double.parseDouble(xmlParser.getAttributeValue(0)));
                                    }
                                    break;
                                case "clouds":
                                    if (xmlParser.getAttributeName(1).equals("all")) {
                                        mCloudsList.add(Integer.parseInt(xmlParser.getAttributeValue(1)));
                                    }
                                    if (xmlParser.getAttributeName(0).equals("value")) {
                                        mWeatherList.add(xmlParser.getAttributeValue(0));
                                    }
                                    break;
                                case "windDirection":
                                    if (xmlParser.getAttributeName(0).equals("deg")) {
                                        mDirectionList.add(Double.parseDouble(xmlParser.getAttributeValue(0)));
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

                for (int i = 0; i < mWeatherList.size(); i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, i);
                    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd y");
                    String date = sdf.format(calendar.getTime());

                    int imgResource = getImage(mWeatherList.get(i));
                    mPressure = mPressureList.get(i);
                    mHumidity = mHumidityList.get(i);
                    mSpeed = mSpeedList.get(i);
                    mClouds = mCloudsList.get(i);
                    mDirection = mDirectionList.get(i);

                    mForecastRecyclerList.add(new WeatherParameters(date, imgResource,
                            new StringBuilder(mMinTempList.get(i)).append("/").append(mMaxTempList.get(i))
                                    .append(" \u00B0C").toString(), weatherParamsInTextView()));

                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }
}