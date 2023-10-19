package org.example.fintech.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class WeatherApi {

    public WeatherApi() {
    }

    public class WeatherResponse {

        private Location location;
        private CurrentWeather current;

        public static class Location {
            @JsonProperty("name")
            private String name;
        }

        public static class CurrentWeather {
            @JsonProperty("temp_c")
            private double temperature;

            private Condition condition;
        }

        public static class Condition {
            @JsonProperty("text")
            private String text;
        }

    }
}