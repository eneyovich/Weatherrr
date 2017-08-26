package com.dzondza.vasya.weatherrr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


/**
 * RecyclerView adapter for lists in 5- and 16- days fragments;
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<WeatherParameters> weatherList;


    RecyclerAdapter(List<WeatherParameters> stringList) {
        weatherList = stringList;
    }


    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_recycler_items_5_and_16_days_layout, parent, false);

        return new RecyclerAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.timeTextView.setText(weatherList.get(position).weatherDate);
        holder.forecastImageView.setImageResource(weatherList.get(position).imageResource);
        holder.tempTextView.setText(weatherList.get(position).temper);
        holder.weatherDataTextView.setText(weatherList.get(position).weatherData);
    }



    @Override
    public int getItemCount() {
        return weatherList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView timeTextView;
        private ImageView forecastImageView;
        private TextView tempTextView;
        private TextView weatherDataTextView;

        ViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.text_view_time);
            forecastImageView = itemView.findViewById(R.id.image_view_forecast);
            tempTextView = itemView.findViewById(R.id.text_view_temp);
            weatherDataTextView = itemView.findViewById(R.id.text_view_weather_data);
        }
    }
}