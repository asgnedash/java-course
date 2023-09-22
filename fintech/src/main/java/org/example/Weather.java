package org.example;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<Integer, String> getRegion() {
        return region;
    }

    public static String getRegionNameById(int regionId) {
        return region.get(regionId);
    }

    public static void setRegion(Map<Integer, String> region) {
        Weather.region = region;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}