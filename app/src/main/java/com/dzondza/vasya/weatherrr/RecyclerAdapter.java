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

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<WeatherParameters> weatherList;

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



    class ViewHolder extends RecyclerView.ViewHolder {

        TextView timeTextView;
        ImageView forecastImageView;
        TextView tempTextView;
        TextView weatherDataTextView;

        ViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.text_time_5_16_days);
            forecastImageView = itemView.findViewById(R.id.image_5_16_days_forecast);
            tempTextView = itemView.findViewById(R.id.text_temp_5_16_days);
            weatherDataTextView = itemView.findViewById(R.id.text_5_16_days_weather_data);
        }
    }
}