package org.example.fintech.service;

import org.example.fintech.entities.WeatherType;
import org.example.fintech.repository.WeatherTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class providing methods to interact with weather type data in the application.
 * This class encapsulates operations such as saving, retrieving, and deleting weather types.
 */

@Service
public class WeatherTypeService {
    private final WeatherTypeRepository weatherTypeRepository;

    @Autowired
    public WeatherTypeService(WeatherTypeRepository weatherTypeRepository) {
        this.weatherTypeRepository = weatherTypeRepository;
    }

    public void saveWeatherType(WeatherType weatherType) {
        weatherTypeRepository.save(weatherType);
    }

    public WeatherType getWeatherTypeById(Long id) {
        return weatherTypeRepository.findById(id).orElse(null);
    }

    public void deleteWeatherTypeById(Long id) {
        weatherTypeRepository.deleteById(id);
    }
}
