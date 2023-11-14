package org.example.fintech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
 * Service class providing methods to perform CRUD operations
 * for weather-related data using JDBC with Spring's JdbcTemplate.
 * This class handles interactions with the weather database table.
 * The class also manages different transaction isolation levels for specific create operations.
 */

@Service
public class WeatherJdbcService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CityJdbcService cityJdbcService;
    @Autowired
    private WeatherTypeJdbcService weatherTypeJdbcService;

    public void create(String city, String type, double temperature, Timestamp timestamp) {
        Boolean isCityExists = true;
        Boolean isWeatherTypeExists = true;
        long city_id = 0, weather_type_id = 0;
        String sqlCityCheck = "SELECT id FROM city WHERE city = ?";
        String sqlWeatherTypeCheck = "SELECT id FROM weathertype WHERE type = ?";
        String sql = "INSERT INTO weather (city_id, weather_type_id, temperature, timestamp) VALUES (?, ?, ?, ?)";

        if (jdbcTemplate.queryForList(sqlCityCheck, city).isEmpty()) {
            isCityExists = false;
        } else if (jdbcTemplate.queryForList(sqlCityCheck, city).size() > 1) {
            isCityExists = false;
            throw new RuntimeException("More than one id for one city");
        } else {
            city_id = jdbcTemplate.queryForObject(sqlCityCheck, Long.class, city);
        }

        if (jdbcTemplate.queryForList(sqlWeatherTypeCheck, type).isEmpty()) {
            isWeatherTypeExists = false;
        } else if (jdbcTemplate.queryForList(sqlWeatherTypeCheck, type).size() > 1) {
            isWeatherTypeExists = false;
            throw new RuntimeException("More than one id for one weather type");
        } else {
            weather_type_id = jdbcTemplate.queryForObject(sqlWeatherTypeCheck, Long.class, type);
        }
        if (isCityExists && isWeatherTypeExists) {
            jdbcTemplate.update(sql, city_id, weather_type_id, temperature, timestamp);
        } else if (isCityExists && !isWeatherTypeExists) {
            weatherTypeJdbcService.create(type);
            jdbcTemplate.update(sql, city_id, weather_type_id, temperature, timestamp);
        } else if (!isCityExists && isWeatherTypeExists) {
            cityJdbcService.create(city);
            jdbcTemplate.update(sql, city_id, weather_type_id, temperature, timestamp);
        } else if (!isCityExists && !isWeatherTypeExists) {
            cityJdbcService.create(city);
            weatherTypeJdbcService.create(type);
            jdbcTemplate.update(sql, city_id, weather_type_id, temperature, timestamp);
        }

    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void createWithReadUncommittedIsolation(String city, String type, double temperature, Timestamp timestamp) {
        create(city, type, temperature, timestamp);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createWithReadCommittedIsolation(String city, String type, double temperature, Timestamp timestamp) {
        create(city, type, temperature, timestamp);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void createWithRepeatableReadIsolation(String city, String type, double temperature, Timestamp timestamp) {
        create(city, type, temperature, timestamp);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createWithSerializableIsolation(String city, String type, double temperature, Timestamp timestamp) {
        create(city, type, temperature, timestamp);
    }

    public String getById(Long id) {
        String sql = "SELECT * " + "FROM weathers w " + "JOIN city c ON w.city_id = c.id " + "JOIN weathertype wt ON w.weather_type_id = wt.id " + "WHERE w.id = ?";
        return jdbcTemplate.queryForList(sql, id).toString();
    }

    public String getAll() {
        String sql = "SELECT * " + "FROM weathers w " + "JOIN city c ON w.city_id = c.id " + "JOIN weathertype wt ON w.weather_type_id = wt.id ";
        return jdbcTemplate.queryForList(sql).toString();
    }

    public void update(long id, double temperature) {
        String sql = "UPDATE weathers SET temperature = ? WHERE id = ?";
        jdbcTemplate.update(sql, temperature, id);
    }

    public void delete(long id) {
        String sql = "DELETE FROM weathers WHERE id = (?)";
        jdbcTemplate.update(sql, id);
    }
}
