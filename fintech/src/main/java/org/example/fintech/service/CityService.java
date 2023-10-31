package org.example.fintech.service;

import org.example.fintech.entities.City;
import org.example.fintech.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class providing methods to perform CRUD operations for City objects
 * using methods from the injected CityRepository.
 */

@Service
public class CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public void saveCity(City city) {
        cityRepository.save(city);
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id).orElse(null);
    }

    public void deleteCityById(Long id) {
        cityRepository.deleteById(id);
    }
}
