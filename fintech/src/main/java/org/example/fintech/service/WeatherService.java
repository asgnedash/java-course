package org.example.fintech.service;

import org.example.fintech.model.Weather;
import org.example.fintech.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class managing weather-related operations.
 * This class provides methods for creating, reading, updating, and deleting weather records
 * using an in-memory list. It also includes static methods for calculating average temperatures,
 * finding regions with temperatures above a specified threshold, and converting weather data
 * into maps for further analysis. Additionally, the class integrates with a WeatherRepository
 * for database operations and uses static initialization for test data.
 * The class is annotated with @Service to indicate that it is a Spring service component.
 * <p>
 * weatherInit()       - Creates a list of Weather objects.
 * avgTemperature()    - Calculates the average temperature for each region.
 * regionsTempOver()   - Finds regions where the temperature could be higher than the specified value.
 * toMapIdTemp()       - Converts the list of Weather objects into a map with regionId in keys and list of temperatures in each region in values.
 * toMapTempWeather()  - Converts the list of Weather objects into a map with temperature in keys and collections of Weather objects with the same temperature as the key in values.
 * <p>
 * createWeather()         - (C) Creates the weather object.
 * readTemperatureByCity() - (R) Reads a temperature value by the specified city.
 * updateWeather()         - (U) Updates existing weather object or creates a new one.
 * deleteWeather()         - (D) Deletes all weather objects with the specified city.
 */

@Service
public class WeatherService {

    static ArrayList<Weather> weatherList;

    static {
        weatherList = weatherInit();
    }

    private WeatherRepository weatherRepository;

    public WeatherService() {
    }

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public static ArrayList<Weather> weatherInit() {
        ArrayList<Weather> weatherList = new ArrayList<>();
        weatherList.add(new Weather("Moscow", -7, LocalDateTime.of(2022, 1, 16, 10, 15, 0)));
        weatherList.add(new Weather("Saint Petersburg", -3, LocalDateTime.of(2023, 2, 15, 12, 10, 0)));
        weatherList.add(new Weather("Tallinn", 4, LocalDateTime.of(2022, 3, 17, 23, 5, 0)));
        weatherList.add(new Weather("Saint Petersburg", 13, LocalDateTime.of(2023, 4, 23, 13, 0, 0)));
        weatherList.add(new Weather("Saint Petersburg", 24, LocalDateTime.of(2023, 5, 30, 16, 35, 0)));
        weatherList.add(new Weather("Moscow", 24, LocalDateTime.of(2023, 6, 1, 17, 30, 0)));
        weatherList.add(new Weather("Moscow", 15, LocalDateTime.now()));
        return weatherList;
    }

    public static void avgTemperature(ArrayList<Weather> weatherList) {
        Map<Integer, Double> avgTemperature = weatherList.stream()
                .collect(Collectors.groupingBy(Weather::getRegionId,
                        Collectors.averagingDouble(Weather::getTemperature)
                ));

        avgTemperature.forEach((regionId, avgTemp)->{
            System.out.println(Weather.getRegionNameById(regionId) + ": " + Math.round(avgTemp * 100) / 100.0);
        });
    }

    public static void regionsTempOver(ArrayList<Weather> weatherList, double temperature) {
        Set<String> regionsAboveThreshold = weatherList.stream()
                .filter(weather->weather.getTemperature() > temperature)
                .map(Weather::getRegionName)
                .collect(Collectors.toSet());
        System.out.println("\nRegions with temperature higher than " + temperature + ": " + regionsAboveThreshold);
    }

    public static Map<Integer, List<Double>> toMapIdTemp(ArrayList<Weather> weatherList) {
        return weatherList.stream()
                .collect(Collectors.groupingBy(Weather::getRegionId,
                        Collectors.mapping(Weather::getTemperature, Collectors.toList())));
    }

    public static Map<Double, List<Weather>> toMapTempWeather(ArrayList<Weather> weatherList) {
        return weatherList.stream()
                .collect(Collectors.groupingBy(Weather::getTemperature));
    }

    public int createWeather(Weather weather) {
        weatherList.add(weather);
        if (weatherList.contains(weather)) {
            return 201;
        } else {
            return 500;
        }
    }

    public Double readTemperatureByCity(String regionName) {
        LocalDate date = LocalDate.now();

        Optional<Double> temperature = weatherList.stream()
                .filter(weather->weather.getRegionName().equals(regionName))
                .filter(weather->weather.getTimestamp().toLocalDate().isEqual(date))
                .map(Weather::getTemperature)
                .findFirst();

        return temperature.orElseGet(()->null);

    }

    public int updateWeather(Weather weather) {
        boolean isInTheList = false;
        for (Weather i : weatherList) {
            if (weather.getRegionId() == i.getRegionId() && weather.getTimestamp() == i.getTimestamp()) {
                i.setTemperature(weather.getTemperature());
                isInTheList = true;
            }
        }
        if (!isInTheList) {
            weatherList.add(weather);
            return 201;
        }
        if (weatherList.contains(weather)) {
            return 200;
        } else {
            return 500;
        }
    }

    public void deleteWeather(String regionName) {
        weatherList.removeIf(region->region.getRegionName().equals(regionName));
    }

    public void saveWeather(org.example.fintech.entities.Weather weather) {
        weatherRepository.save(weather);
    }

    public org.example.fintech.entities.Weather getWeatherById(Long id) {
        return weatherRepository.findById(id).orElse(null);
    }

    public void deleteWeatherById(Long id) {
        weatherRepository.deleteById(id);
    }

}
