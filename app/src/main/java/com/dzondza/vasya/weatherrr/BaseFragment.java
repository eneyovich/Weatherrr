package com.dzondza.vasya.weatherrr;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * basic fragment for today, 5- and 16- days fragments
 */

public abstract class BaseFragment extends Fragment {

    BufferedReader reader;
    int humidity, clouds;
    double temp, pressure, speed, direction;
    RecyclerAdapter recyclerAdapter;
    protected List<WeatherParameters> forecastRecyclerList;
    List<Double> temperList = new ArrayList<>();
    List<Double> directionList = new ArrayList<>();
    List<Integer> humidityList = new ArrayList<>();
    List<Double> speedList = new ArrayList<>();
    List<Integer> cloudsList = new ArrayList<>();
    List<Double> pressureList = new ArrayList<>();
    List<String> weatherList = new ArrayList<>();


    protected void initializeRecycler (View view){
        forecastRecyclerList = new ArrayList<>();

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_id);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        recyclerAdapter = new RecyclerAdapter(forecastRecyclerList);
        mRecyclerView.setAdapter(recyclerAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                manager.getOrientation()));

        mRecyclerView.setHasFixedSize(true);
    }


    //sets weather params in texView in 5 & 16 days fragments
    protected String weatherParamsInTextView() {
        return "pressure " + pressure + " hPa\nhumidity " + humidity + " %\nspeed " + speed
                + " m/s\nclouds " + clouds + " %\ndirection " + direction + " deg";
    }


    // opens HttpURLConnection and returns response in JSON format
    protected JSONObject getBasicJson(String urlBeginning, String city, String urlEnd) {
        try {
            URL url = new URL(urlBeginning + city + urlEnd);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String tmp;
            StringBuffer buffer = new StringBuffer(1024);

            while ((tmp = reader.readLine()) != null) {
                buffer.append(tmp);
            }
            JSONObject jsonObj = new JSONObject(buffer.toString());

            if (jsonObj.getInt("cod") != 200) {
                Toast.makeText(getActivity(), getString(R.string.loading_error), Toast.LENGTH_SHORT).show();
            }
            reader.close();

            connection.disconnect();

            return jsonObj;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //chooses image resource id to descript forecast
    protected int getImage(String weatherDescript) {
        int imageResource;
        switch (weatherDescript) {
            case "light rain":
            case "light intensity shower rain":
                imageResource = R.drawable.raining_little;
                break;
            case "overcast clouds":
            case "haze":
                imageResource = R.drawable.foggy_much;
                break;
            case "moderate rain":
            case "heavy intensity rain":
            case "shower rain":
                imageResource = R.drawable.raining_medium;
                break;
            case "broken clouds":
                imageResource = R.drawable.foggy_medium;
                break;
            case "scattered clouds":
            case "few clouds":
                imageResource = R.drawable.foggy_minimum;
                break;
            case "clear sky":
            case "sky is clear":
                imageResource = R.drawable.sunny;
                break;
            default:
                imageResource = R.drawable.unknown;
        }
        return imageResource;
    }

    //creates xmlParser and gets data in xml format
    protected XmlPullParser registerXMLParser(String urlBegin, String city, String urlEnd,
                                              BufferedReader reader) {
        try {
            URL url = new URL(urlBegin + city + urlEnd);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(reader);

            if (xpp.getEventType() == XmlPullParser.END_DOCUMENT){
                reader.close();
            }
            return xpp;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    protected abstract void getDataFromXML(final String city);
}