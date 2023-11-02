package org.example.fintech.service;

import org.example.fintech.controller.WeatherApiErrorController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class WeatherApiServiceTests {

    @Autowired
    private WeatherApiService weatherApiService;

    @Test
    public void happyPath() {
        Assertions.assertNotEquals(null, weatherApiService.getCurrentWeather("Moscow"));
    }

    @Test
    public void testGetCurrentWeather() {
        String city = "Vologda";

        StepVerifier.create(weatherApiService.getCurrentWeather(city))
                .expectNextMatches(response -> response.contains("Vologda"))
                .verifyComplete();
    }

    @Test
    public void testGetCurrentWeatherWithWrongCity() {
        String city = "Moskow";

        StepVerifier.create(weatherApiService.getCurrentWeather(city))
                .expectErrorMatches(throwable -> throwable instanceof WeatherApiErrorController &&
                        ((WeatherApiErrorController) throwable).getErrorMessage().contains("No location found matching parameter 'q'"))
                .verify();
    }

    @Test
    public void testGetCurrentWeatherWithEmptyCity() {
        String city = "";

        StepVerifier.create(weatherApiService.getCurrentWeather(city))
                .expectErrorMatches(throwable -> throwable instanceof WeatherApiErrorController &&
                        ((WeatherApiErrorController) throwable).getErrorMessage().contains("Parameter 'q' not provided"))
                .verify();
    }
}
