package org.example.fintech.service;

import org.example.fintech.model.Weather;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    /*
        weatherInit()       - Creates a list of Weather objects.
        avgTemperature()    - Calculates the average temperature for each region.
        regionsTempOver()   - Finds regions where the temperature could be higher than the specified value.
        toMapIdTemp()       - Converts the list of Weather objects into a map with regionId in keys and list of temperatures in each region in values.
        toMapTempWeather()  - Converts the list of Weather objects into a map with temperature in keys and collections of Weather objects with the same temperature as the key in values.

        createWeather()         - (C) Creates the weather object.
        readTemperatureByCity() - (R) Reads a temperature value by the specified city.
        updateWeather()         - (U) Updates existing weather object or creates a new one.
        deleteWeather()         - (D) Deletes all weather objects with the specified city.
    */

    static ArrayList<Weather> weatherList;

    static {
        weatherList = weatherInit();
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
                .filter(weather -> weather.getRegionName().equals(regionName))
                .filter(weather -> weather.getTimestamp().toLocalDate().isEqual(date))
                .map(Weather::getTemperature)
                .findFirst();

        return temperature.orElseGet(() -> null);

    }

    public int updateWeather(Weather weather) {
        boolean isInTheList = false;
        for (Weather i: weatherList) {
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
        weatherList.removeIf(region -> region.getRegionName().equals(regionName));
    }

    public static ArrayList<Weather> weatherInit() {
        ArrayList<Weather> weatherList = new ArrayList<>();
        weatherList.add(new Weather("Moscow", -7, LocalDateTime.of(2022, 1, 16, 10, 15, 0)));
        weatherList.add(new Weather("Saint Petersburg", -3, LocalDateTime.of(2023, 2, 15, 12, 10, 0)));
        weatherList.add(new Weather("Tallinn", 4, LocalDateTime.of(2022, 3, 17, 23, 5, 0)));
        weatherList.add(new Weather("Saint Petersburg", 13, LocalDateTime.of(2023, 4, 23, 13, 0, 0)));
        weatherList.add(new Weather("Saint Petersburg", 24, LocalDateTime.of(2023, 5, 30, 16, 35, 0)));
        weatherList.add(new Weather("Moscow", 24, LocalDateTime.of(2023, 6, 1, 17, 30, 0)));
        //for testing readTemperatureByCity function:
        weatherList.add(new Weather("Moscow", 15, LocalDateTime.now()));
        return weatherList;
    }

    public static void avgTemperature(ArrayList<Weather> weatherList) {
        Map<Integer, Double> avgTemperature = weatherList.stream()
                .collect(Collectors.groupingBy(Weather::getRegionId,
                        Collectors.averagingDouble(Weather::getTemperature)
                ));

        avgTemperature.forEach((regionId, avgTemp) -> {
            System.out.println(Weather.getRegionNameById(regionId) + ": " + Math.round(avgTemp * 100) / 100.0);
        });
    }

    public static void regionsTempOver(ArrayList<Weather> weatherList, double temperature) {
        Set<String> regionsAboveThreshold = weatherList.stream()
                .filter(weather -> weather.getTemperature() > temperature)
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

    /*
    public static void main(String[] args) {

        ArrayList<Weather> weatherList = weatherInit();

        System.out.println("Average regions temperature:");
        avgTemperature(weatherList);

        regionsTempOver(weatherList, 5.0);

        System.out.println("\nA map with unique region identifiers and temperature values:");
        toMapIdTemp(weatherList).forEach((regionId, temperatures) -> {
            System.out.println(regionId + ": " + temperatures.toString());
        });

        System.out.println("\nA map with temperature as the key and a collection of Weather objects:");
        toMapTempWeather(weatherList).forEach((temperature, weatherObjects) -> {
            System.out.println(temperature + " : " + weatherObjects.toString());
        });

    }

     */

}
