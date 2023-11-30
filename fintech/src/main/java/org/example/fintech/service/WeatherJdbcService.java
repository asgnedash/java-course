package org.example.fintech.service;

import org.example.fintech.DTO.WeatherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public WeatherJdbcService(JdbcTemplate jdbcTemplate, CityJdbcService cityJdbcService, WeatherTypeJdbcService weatherTypeJdbcService) {
        this.jdbcTemplate = jdbcTemplate;
        this.cityJdbcService = cityJdbcService;
        this.weatherTypeJdbcService = weatherTypeJdbcService;
    }

    @Transactional
    public void create(String city, String type, double temperature, Timestamp timestamp) {
        Boolean isCityExists = true;
        Boolean isWeatherTypeExists = true;
        long city_id = 1, weather_type_id = 1;
        String sqlCityCheck = "SELECT id FROM cities WHERE city = ?";
        String sqlWeatherTypeCheck = "SELECT id FROM weather_types WHERE type = ?";
        String sql = "INSERT INTO weathers (city_id, weather_type_id, temperature, timestamp) VALUES (?, ?, ?, ?)";

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
            System.out.println(weather_type_id);
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

    @Transactional
    public void create(WeatherDTO weatherDTO) {
        String city = weatherDTO.getCity();
        String type = weatherDTO.getWeatherType();
        double temperature = weatherDTO.getTemperature();
        LocalDateTime timestamp = weatherDTO.getTimestamp();
        Boolean isCityExists = true;
        Boolean isWeatherTypeExists = true;
        long city_id = 1, weather_type_id = 1;
        String sqlCityCheck = "SELECT id FROM cities WHERE city = ?";
        String sqlWeatherTypeCheck = "SELECT id FROM weather_types WHERE type = ?";
        String sql = "INSERT INTO weathers (city_id, weather_type_id, temperature, timestamp) VALUES (?, ?, ?, ?)";

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
            System.out.println(weather_type_id);
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
        String sql = "SELECT * " + "FROM weathers w " + "JOIN cities c ON w.city_id = c.id " + "JOIN weather_types wt ON w.weather_type_id = wt.id " + "WHERE w.id = ?";
        return jdbcTemplate.queryForList(sql, id).toString();
    }

    public String getAll() {
        String sql = "SELECT * " + "FROM weathers w " + "JOIN cities c ON w.city_id = c.id " + "JOIN weather_types wt ON w.weather_type_id = wt.id ";
        return jdbcTemplate.queryForList(sql).toString();
    }

    public List<Float> getListOfTemperaturesByCity(String city) {
        String sql = "SELECT temperature FROM weathers w JOIN cities c ON w.city_id = c.id WHERE c.city = ?";
        if (jdbcTemplate.queryForList(sql, Double.class, city).isEmpty()) {
            return null;
        } else {
            return jdbcTemplate.queryForList(sql, Float.class, city);
        }
    }

    public void update(long id, double temperature) {
        String sql = "UPDATE weathers SET temperature = ? WHERE id = ?";
        jdbcTemplate.update(sql, temperature, id);
    }

    public void delete(long id) {
        String sql = "DELETE FROM weathers WHERE id = (?)";
        jdbcTemplate.update(sql, id);
    }

    public void deleteByCity(String city) {
        String sql = "DELETE FROM weathers WHERE city_id IN (SELECT id FROM cities WHERE city = ?)";
        jdbcTemplate.update(sql, city);
    }

    public void deleteLatestByCity(String city) {
        String sql = "DELETE FROM weathers WHERE id IN (SELECT w.id FROM weathers w " +
                "JOIN cities c ON w.city_id = c.id WHERE city = ? ORDER BY w.timestamp DESC LIMIT 1)";
        jdbcTemplate.update(sql, city);
    }

    public WeatherDTO getLatestByCity(String city) {
        String sql = "SELECT * FROM weathers w JOIN cities c ON w.city_id = c.id JOIN weather_types wt ON w.weather_type_id = wt.id WHERE w.id IN (SELECT w.id FROM weathers w " +
                "JOIN cities c ON w.city_id = c.id WHERE city = ? ORDER BY w.timestamp DESC LIMIT 1)";

        Map<String, Object> result = jdbcTemplate.queryForMap(sql, city);
        return new WeatherDTO(
                (String) result.get("CITY"),
                (String) result.get("TYPE"),
                (Double) result.get("TEMPERATURE"),
                ((Timestamp) result.get("TIMESTAMP")).toLocalDateTime()
        );
    }

}
