package org.example.fintech.cache;

import org.example.fintech.entities.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.ls.LSOutput;

import java.util.*;

public class WeatherCache {
    @Value("${cache.capacity}")
    private final int capacity;
    Map<String, Weather> cache;

    public WeatherCache(int capacity) {
        this.capacity = capacity;
        this.cache = Collections.synchronizedMap(new LinkedHashMap<String, Weather>(capacity + 1, 1.0f, true)
        {
            @Override
            public boolean removeEldestEntry(Map.Entry<String, Weather> eldest) {
                return size() > capacity;
            }
        });
    }

    public Optional<Weather> get(String city) {
        return Optional.ofNullable(cache.get(city));
    }

    public void put(Weather weather) {
        cache.put(weather.getCity().getCity(), weather);
    }

    public void remove(String city) {
        cache.remove(city);
    }
}
