package org.example.fintech.repository;

import org.example.fintech.entities.Weather;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<Weather, Long> {
}
