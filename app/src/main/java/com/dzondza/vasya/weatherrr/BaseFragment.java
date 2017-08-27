package com.dzondza.vasya.weatherrr;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
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

    private JSONObject mBasicJson;
    private int mHumidity, mClouds;
    private double mTemp, mMinTemp, mMaxTemp, mPressure, mSpeed, mDirection;
    RecyclerAdapter mRecyclerAdapter;
    List<WeatherParameters> mForecastRecyclerList;


    protected void initializeRecycler (View view){
        mForecastRecyclerList = new ArrayList<>();

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_id);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);

        mRecyclerAdapter = new RecyclerAdapter(mForecastRecyclerList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(),
                manager.getOrientation()));

        mRecyclerView.setHasFixedSize(true);
    }


    //sets weather parameters in texView in 5 & 16 days fragments
    String weatherParamsInTextView() {
        return "mPressure " + mPressure + " hPa\nmHumidity " + mHumidity + " %\nmSpeed " + mSpeed
                + " m/s\nmClouds " + mClouds + " %\nmDirection " + mDirection + " deg";
    }


    // opens HttpURLConnection and returns response in JSON format
    JSONObject initializeBasicJson(String urlBeginning, String city, String urlEnd) {
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
    int getImage(String weatherDescript) {
        int imageResource;
        switch (weatherDescript) {
            case "light rain":
            case "light intensity shower rain":
                imageResource = R.drawable.drawable_rain_little;
                break;
            case "overcast mClouds":
            case "haze":
                imageResource = R.drawable.drawable_foggy_much;
                break;
            case "moderate rain":
            case "heavy intensity rain":
            case "shower rain":
                imageResource = R.drawable.drawable_raining_medium;
                break;
            case "broken mClouds":
                imageResource = R.drawable.drawable_foggy_medium;
                break;
            case "scattered mClouds":
            case "few mClouds":
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


    abstract void getJSON(final String city);


    public JSONObject getBasicJson() {
        return mBasicJson;
    }

    public void setBasicJson(JSONObject basicJson) {
        this.mBasicJson = basicJson;
    }

    public int getHumidity() {
        return mHumidity;
    }

    public void setHumidity(int humidity) {
        this.mHumidity = humidity;
    }

    public int getClouds() {
        return mClouds;
    }

    public void setClouds(int clouds) {
        this.mClouds = clouds;
    }

    public double getTemp() {
        return mTemp;
    }

    public void setTemp(double temp) {
        this.mTemp = temp;
    }

    public double getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(double minTemp) {
        this.mMinTemp = minTemp;
    }

    public double getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.mMaxTemp = maxTemp;
    }

    public double getPressure() {
        return mPressure;
    }

    public void setPressure(double pressure) {
        this.mPressure = pressure;
    }

    public double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public double getDirection() {
        return mDirection;
    }

    public void setDirection(double direction) {
        this.mDirection = direction;
    }
}