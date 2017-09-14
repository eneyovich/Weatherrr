package com.dzondza.vasya.weatherrr.GsonStructure;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * GSON structure for SixteenDaysFragment
 */

public class SixteenDaysGsonStructure {

    @SerializedName("list")
    private List<WeatherList> list = new ArrayList<>();

    public List<WeatherList> getList() {
        return list;
    }

    public class WeatherList {
        private double pressure;
        private int humidity;
        private double speed;
        private int clouds;
        private int deg;
        public WeatherList.Temp temp;
        public WeatherList.Weather[] weather;

        public class Temp {
            private double min;
            private double max;

            //getters

            public double getMin() {
                return min;
            }

            public double getMax() {
                return max;
            }
        }

        public class Weather {
            private String description;

            //getter
            public String getDescription() {
                return description;
            }
        }


        // getters

        public double getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public double getSpeed() {
            return speed;
        }

        public int getClouds() {
            return clouds;
        }

        public int getDeg() {
            return deg;
        }
    }
}