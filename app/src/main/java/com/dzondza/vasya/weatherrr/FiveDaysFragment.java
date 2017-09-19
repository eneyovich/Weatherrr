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

    private List<Double> mTempList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
                    XmlPullParser xmlParser = registerXMLParser("http://api.openweathermap.org/data/2.5/forecast?q=",
                            city, "&mode=xml&units=metric&APPID=419b4a7ba318ef5286319f89b37ed373");

                    while (xmlParser.getEventType() != XmlPullParser.END_DOCUMENT) {

                        if (xmlParser.getEventType() == XmlPullParser.START_TAG) {

                            switch (xmlParser.getName()) {

                                case "temperature":
                                    if (xmlParser.getAttributeName(1).equals("value")) {
                                        mTempList.add(Double.parseDouble(xmlParser.getAttributeValue(1)));
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
                    calendar.add(Calendar.HOUR_OF_DAY, i*3);
                    Date time = calendar.getTime();

                    int imgResource = getImage(mWeatherList.get(i));
                    mPressure = mPressureList.get(i);
                    mHumidity = mHumidityList.get(i);
                    mSpeed = mSpeedList.get(i);
                    mClouds = mCloudsList.get(i);
                    mDirection = mDirectionList.get(i);

                    mForecastRecyclerList.add(new WeatherParameters(time.toString(), imgResource,
                            mTempList.get(i) + " \u00B0C", weatherParamsInTextView()));

                    mRecyclerAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }
}