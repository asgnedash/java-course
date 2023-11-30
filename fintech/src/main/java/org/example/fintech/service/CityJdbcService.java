package org.example.fintech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Service class providing methods to perform CRUD operations
 * for city entities using JDBC with Spring's JdbcTemplate.
 * This class is responsible for interacting with the database to manage city data.
 */

@Service
public class CityJdbcService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void create(String city) {
        String sql = "INSERT INTO cities (city) VALUES (?)";
        jdbcTemplate.update(sql, city);
    }

    public String getById(Long id) {
        String sql = "SELECT * FROM cities WHERE id = ?";
        return jdbcTemplate.queryForList(sql, id).toString();
    }

    public String getAll() {
        String sql = "SELECT * FROM cities";
        return jdbcTemplate.queryForList(sql).toString();
    }

    public void update(long id, String city) {
        String sql = "UPDATE cities SET city = ? WHERE id = ?";
        jdbcTemplate.update(sql, city, id);
    }

    public void delete(long id) {
        String sql = "DELETE FROM cities WHERE id = (?)";
        jdbcTemplate.update(sql, id);
    }
}
