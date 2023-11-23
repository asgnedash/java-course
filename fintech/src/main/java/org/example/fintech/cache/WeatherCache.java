package org.example.fintech.cache;

import org.example.fintech.DTO.WeatherDTO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class WeatherCache {

    private int capacity;

    @Override
    public String toString() {
        return "WeatherCache{" +
                "capacity=" + capacity +
                ", cache=" + cache +
                '}';
    }

    private Map<String, WeatherDTO> cache;

    public WeatherCache() {
        Properties properties = new Properties();
        try {
            properties.load(Objects.requireNonNull(WeatherCache.class.getClassLoader().getResourceAsStream("application.properties")));
            this.capacity = Integer.parseInt(properties.getProperty("cache.course.size"));
        } catch (IOException | NullPointerException | NumberFormatException e) {
            e.printStackTrace();
        }

        this.cache = Collections.synchronizedMap(new LinkedHashMap<String, WeatherDTO>(capacity + 1, 1.0f, true)
        {
            @Override
            public boolean removeEldestEntry(Map.Entry<String, WeatherDTO> eldest) {
                return size() > capacity;
            }
        });
    }

    public Optional<WeatherDTO> get(String city) {
        return Optional.ofNullable(cache.get(city));
    }

    public void put(String city, String weatherType, Double temperature, LocalDateTime timestamp) {
        WeatherDTO dto = new WeatherDTO();
        dto.setCity(city);
        dto.setTemperature(temperature);
        dto.setWeatherType(weatherType);
        dto.setTimestamp(LocalDateTime.now());
        cache.put(city, dto);
    }

    public void remove(String city) {
        cache.remove(city);
    }
}
