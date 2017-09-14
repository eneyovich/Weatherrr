package com.dzondza.vasya.weatherrr.GsonStructure;

/**
 * GSON structure for TodayFragment
 */

public class TodayGsonStructure {

    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Weather[] weather;

    public class Main {
        private double temp;
        private double pressure;
        private int humidity;

        //getters

        public double getTemp() {
            return temp;
        }

        public double getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public class Wind {
        private double speed;
        private double deg;

        //getters

        public double getSpeed() {
            return speed;
        }

        public double getDeg() {
            return deg;
        }
    }

    public class Clouds {
        private int all;

        public int getAll() {
            return all;
        }
    }

    public class Weather {
        private String description;

        public String getDescription() {
            return description;
        }
    }

    //getters

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Weather[] getWeather() {
        return weather;
    }
}