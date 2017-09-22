package com.dzondza.vasya.weatherrr;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import org.json.JSONObject;
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

    protected JSONObject mBasicJson;
    protected int mHumidity, mClouds;
    protected double mTemp, mMinTemp, mMaxTemp, mPressure, mSpeed, mDirection;
    protected RecyclerAdapter mRecyclerAdapter;
    protected List<WeatherParameters> mForecastRecyclerList;


    protected void initializeRecycler (View view){
        mForecastRecyclerList = new ArrayList<>();

        RecyclerView mRecyclerView = view.findViewById(R.id.my_recycler_view);

        mRecyclerAdapter = new RecyclerAdapter(mForecastRecyclerList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                ((LinearLayoutManager)mRecyclerView.getLayoutManager()).getOrientation()));

        mRecyclerView.setHasFixedSize(true);
    }


    //sets weather parameters in texView in 5 & 16 days fragments
    protected String weatherParamsInTextView() {
        return "Pressure " + mPressure + " hPa\nHumidity " + mHumidity + " %\nSpeed " + mSpeed
                + " m/s\nClouds " + mClouds + " %\nDirection " + mDirection + " deg";
    }


    // opens HttpURLConnection and returns response in JSON format
    protected JSONObject initializeBasicJson(String urlBeginning, String city, String urlEnd) {
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
                Log.i("COD_ERROR", getString(R.string.loading_error));
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
                imageResource = R.drawable.drawable_rain_little;
                break;
            case "overcast clouds":
            case "haze":
                imageResource = R.drawable.drawable_foggy_much;
                break;
            case "moderate rain":
            case "heavy intensity rain":
            case "shower rain":
                imageResource = R.drawable.drawable_raining_medium;
                break;
            case "broken clouds":
                imageResource = R.drawable.drawable_foggy_medium;
                break;
            case "scattered clouds":
            case "few clouds":
                imageResource = R.drawable.drawable_foggy_minimum;
                break;
            case "clear sky":
            case "sky is clear":
                imageResource = R.drawable.drawable_sunny;
                break;
            default:
                imageResource = R.drawable.drawable_unknown;
        }
        return imageResource;
    }


    protected abstract void getJSON(final String city);
}