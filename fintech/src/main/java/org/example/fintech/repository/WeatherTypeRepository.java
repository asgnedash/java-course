package org.example.fintech.repository;

import org.example.fintech.entities.WeatherType;
import org.springframework.data.repository.CrudRepository;

public interface WeatherTypeRepository extends CrudRepository<WeatherType, Long> {
}