package org.example.fintech.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fintech.DTO.WeatherDTO;
import org.example.fintech.service.WeatherJdbcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class KafkaConsumer {

    private static final int MAX_CITIES_NUM = 6; // 5 cities times 6 equals 30. It's 6 weathers for every city for 30 minutes

    @Autowired
    public KafkaConsumer(WeatherJdbcService weatherJdbcService) {
        this.weatherJdbcService = weatherJdbcService;
    }

    private final WeatherJdbcService weatherJdbcService;


    @KafkaListener(topics = KafkaProducer.TOPIC, groupId = "weather-group-id")
    public void weatherDataConsumer(WeatherDTO weatherDTO) {
        if (weatherJdbcService.getListOfTemperaturesByCity(weatherDTO.getCity()) != null) {
            if (weatherJdbcService.getListOfTemperaturesByCity(weatherDTO.getCity()).size() == MAX_CITIES_NUM) {
                List<Float> temperatures = weatherJdbcService.getListOfTemperaturesByCity(weatherDTO.getCity());
                log.info("Average temperature for {} is {}", weatherDTO.getCity(), temperatures.stream()
                        .mapToDouble(Float::doubleValue)
                        .average()
                        .orElse(0.0));
                weatherJdbcService.deleteLatestByCity(weatherDTO.getCity());
            }
        }
    }
}
