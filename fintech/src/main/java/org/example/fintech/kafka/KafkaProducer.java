package org.example.fintech.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fintech.DTO.WeatherDTO;
import org.example.fintech.controller.WeatherApiController;
import org.example.fintech.service.WeatherJdbcService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final WeatherApiController weatherApiController;
    private final WeatherJdbcService weatherJdbcService;

    public static final String TOPIC = "weather-topic";

    private final KafkaTemplate<String, WeatherDTO> kafkaTemplate;

    /*
     * Entering the numbers of the required cities to avoid overloading Kafka with all the requests simultaneously.
     * 0 - Moscow
     * 1 - Saint Petersburg
     * 2 - Rostov
     * 3 - Krasnodar
     * 4 - Minsk
     */
    private int cityNumber = 0;

    @Scheduled(cron = "0 * * * * ?")
    public void sendWeatherData() throws JsonProcessingException {
        String city = "";
        switch (cityNumber) {
            case 0 -> city = "Moscow";
            case 1 -> city = "Saint Petersburg";
            case 2 -> city = "Rostov";
            case 3 -> city = "Krasnodar";
            case 4 -> city = "Minsk";
        }
        weatherApiController.getWeather(city);

        kafkaTemplate.send(TOPIC, city, weatherJdbcService.getLatestByCity(city));

        cityNumber += 1;
        cityNumber %= 5;
    }

}
