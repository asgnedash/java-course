package org.example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter  @Getter
public class Weather {

    private static Map<Integer, String> region = new HashMap<>();
    private int regionId;
    private String regionName;
    private double temperature;
    private LocalDateTime timestamp;

    public Weather(int regionId, String regionName, double temperature, LocalDateTime timestamp) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.temperature = temperature;
        this.timestamp = timestamp;
        Weather.region.put(regionId, regionName);
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
        return region.get(regionId);
    }
}
