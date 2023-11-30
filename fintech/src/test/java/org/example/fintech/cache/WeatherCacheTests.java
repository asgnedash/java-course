package org.example.fintech.cache;

import org.example.fintech.DTO.WeatherDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions .*;

public class WeatherCacheTests {

    @Test
    void testGetExistingCity() {
        WeatherCache weatherCache = new WeatherCache();
        weatherCache.put("Moscow", "Sunny", 5.2, LocalDateTime.now());

        assertThat(weatherCache.get("Moscow")).isPresent();
    }

    @Test
    void testGetNonExistingCity() {
        WeatherCache weatherCache = new WeatherCache();
        assertThat(weatherCache.get("Moskow")).isEmpty();
    }

    @Test
    void testPutNewCity() {
        WeatherCache weatherCache = new WeatherCache();
        weatherCache.put("Saint Petersburg", "Cloudy", -3.2, LocalDateTime.now());

        assertThat(weatherCache.get("Saint Petersburg")).isPresent();
    }

    @Test
    void testPutUpdateCity() {
        WeatherCache weatherCache = new WeatherCache();
        weatherCache.put("Kaluga", "Rainy", 1.1, LocalDateTime.now());
        weatherCache.put("Kaluga", "Snowy", -1.1, LocalDateTime.now());

        assertThat(weatherCache.get("Kaluga").map(WeatherDTO::getWeatherType)).contains("Snowy");
    }

    @Test
    void testRemoveCity() {
        WeatherCache weatherCache = new WeatherCache();
        weatherCache.put("Lipetsk", "Foggy", 1.0, LocalDateTime.now());
        weatherCache.remove("Lipetsk");

        assertThat(weatherCache.get("Lipetsk")).isEmpty();
    }
}

