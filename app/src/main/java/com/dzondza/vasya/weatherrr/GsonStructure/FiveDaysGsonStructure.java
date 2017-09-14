package com.dzondza.vasya.weatherrr.GsonStructure;

/**
 * GSON structure for FiveDaysFragment
 */

public class FiveDaysGsonStructure {
    private List[] list = null;

    public List[] getList() {
        return list;
    }

    public class List {
        public Main main;
        public Wind wind;
        public Clouds clouds;
        public Weather[] weather;

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

        public class Weather {
            private String description;

            //getter
            public String getDescription() {
                return description;
            }
        }

        public class Clouds {
            private int all;

            //getter
            public int getAll() {
                return all;
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
    }
}