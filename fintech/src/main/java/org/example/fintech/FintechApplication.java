package org.example.fintech;

import org.example.fintech.entities.City;
import org.example.fintech.entities.Weather;
import org.example.fintech.entities.WeatherType;
import org.example.fintech.service.CityService;
import org.example.fintech.service.WeatherService;
import org.example.fintech.service.WeatherTypeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;

@SpringBootApplication
public class FintechApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintechApplication.class, args);

        /*
        Testing the functionality of Hibernate
         */

        /*
        ConfigurableApplicationContext context = SpringApplication.run(FintechApplication.class, args);

        CityService cityService = context.getBean(CityService.class);
        WeatherService weatherService = context.getBean(WeatherService.class);
        WeatherTypeService weatherTypeService = context.getBean(WeatherTypeService.class);

        City city = new City();
        city.setCity("Kazan");
        cityService.saveCity(city);

        WeatherType weatherType = new WeatherType();
        weatherType.setType("Haze");
        weatherTypeService.saveWeatherType(weatherType);

        Weather weather = new Weather();
        weather.setCity(city);
        weather.setWeatherType(weatherType);
        weather.setTemperature(12.3f);
        weather.setTimestamp(LocalDateTime.now());
        weatherService.saveWeather(weather);

        City retrievedCity = cityService.getCityById(city.getId());
        System.out.println("City: " + retrievedCity.getCity());

        WeatherType retrievedWeatherType = weatherTypeService.getWeatherTypeById(weatherType.getId());
        System.out.println("Weather Type: " + retrievedWeatherType.getType());

        Weather retrievedWeather = weatherService.getWeatherById(weather.getId());
        System.out.println("Temperature: " + retrievedWeather.getTemperature());

        retrievedCity.setCity("Kemerovo");
        cityService.saveCity(retrievedCity);

        retrievedWeather.setTemperature(5.3f);
        weatherService.saveWeather(retrievedWeather);

        weatherService.deleteWeatherById(retrievedWeather.getId());
        cityService.deleteCityById(retrievedCity.getId());
        weatherTypeService.deleteWeatherTypeById(retrievedWeatherType.getId());

        context.close();

         */
    }

}
