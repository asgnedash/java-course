package org.example.fintech.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Setter  @Getter
public class Weather {

    private static int id = 1;
    private static Map<String, Integer> region = new HashMap<String, Integer>();

    @JsonProperty("region_id")
    private int regionId;

    @JsonProperty("region_name")
    private String regionName;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    public Weather(String regionName, double temperature, LocalDateTime timestamp) {
        this.regionName = regionName;
        this.temperature = temperature;
        this.timestamp = timestamp;
        if (region.containsKey(regionName)) {
            this.regionId = region.get(regionName);
        } else {
            this.regionId = id++;
        }
        region.put(regionName, regionId);
    }

    @Override
    public String toString() {
        return "Weather{" +
                "regionId=" + regionId +
                ", regionName='" + regionName + '\'' +
                ", temperature=" + temperature +
                ", timestamp=" + timestamp +
                '}';
    }

    public static String getRegionNameById(int regionId) {
        for (Map.Entry<String, Integer> entry : region.entrySet()) {
            if (entry.getValue() == regionId) {
                return entry.getKey();
            }
        }
        return null;
    }
}
