package org.example.fintech;

import org.example.fintech.cache.WeatherCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class FintechApplication {

    /*Testing of functionality of JDBC*/

    /*
    private final CityJdbcService cityJdbcService;
    private final WeatherTypeJdbcService weatherTypeJdbcService;
    private final WeatherJdbcService weatherJdbcService;

    @Autowired
    public FintechApplication(CityJdbcService cityJdbcService, WeatherTypeJdbcService weatherTypeJdbcService, WeatherJdbcService weatherJdbcService) {
        this.cityJdbcService = cityJdbcService;
        this.weatherTypeJdbcService = weatherTypeJdbcService;
        this.weatherJdbcService = weatherJdbcService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void testJdbc() {

        cityJdbcService.create("Anapa");
        System.out.println(cityJdbcService.getById(807L));
        System.out.println(cityJdbcService.getAll());
        cityJdbcService.update(807L, "Gelendzhik");
        System.out.println(cityJdbcService.getById(807L));
        cityJdbcService.delete(807L);
        System.out.println(cityJdbcService.getById(807L));

        weatherTypeJdbcService.create("Haze");
        System.out.println(weatherTypeJdbcService.getAll());
        weatherTypeJdbcService.update(165L, "Rain");
        System.out.println(weatherTypeJdbcService.getById(165L));
        weatherTypeJdbcService.delete(165L);


        weatherJdbcService.create("Perm", "Snow", 12.3, Timestamp.valueOf(LocalDateTime.now()));
        System.out.println(weatherJdbcService.getAll());
        weatherJdbcService.update(2L, -20.1);
        System.out.println(weatherJdbcService.getById(2L));
        weatherJdbcService.delete(2L);
        System.out.println(weatherJdbcService.getById(2L));
    }

     */

    @EventListener(ApplicationReadyEvent.class)
    public void loadCache() {
        WeatherCache weatherCache = new WeatherCache();
    }


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
