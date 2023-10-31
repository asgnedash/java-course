package org.example.fintech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class providing methods to perform CRUD operations
 * for weather types using JDBC with Spring's JdbcTemplate.
 * This class is responsible for interacting with the weathertype database table.
 */

@Service
public class WeatherTypeJdbcService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void create(String type) {
        String sql = "INSERT INTO weathertype (type) VALUES (?)";
        jdbcTemplate.update(sql, type);
    }

    public String getById(Long id) {
        String sql = "SELECT * FROM weathertype WHERE id = (?)";
        return jdbcTemplate.queryForList(sql, id).toString();
    }

    public String getAll() {
        String sql = "SELECT * FROM weathertype";
        return jdbcTemplate.queryForList(sql).toString();
    }

    public void update(long id, String type) {
        String sql = "UPDATE weathertype SET type = ? WHERE id = ?";
        jdbcTemplate.update(sql, type, id);
    }

    public void delete(long id) {
        String sql = "DELETE FROM weathertype WHERE id = (?)";
        jdbcTemplate.update(sql, id);
    }
}

