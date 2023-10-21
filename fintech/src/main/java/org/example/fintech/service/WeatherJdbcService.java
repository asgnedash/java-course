package org.example.fintech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.*;

@Service
public class WeatherJdbcService {

    private TransactionTemplate transactionTemplate;

    @Autowired
    public WeatherJdbcService(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    public void create(String city, String type, double temperature, Timestamp timestamp) {
        Boolean isCityExists = true;
        Boolean isWeatherTypeExists = true;
        long city_id = 0, weather_type_id = 0;
        String sqlCityCheck = "SELECT * FROM city WHERE city = ?";
        String sqlWeatherTypeCheck = "SELECT * FROM weathertype WHERE type = ?";
        String sql = "INSERT INTO weather (city_id, weather_type_id, temperature, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatementCityCheck = con.prepareStatement(sqlCityCheck, Statement.RETURN_GENERATED_KEYS);
            preparedStatementCityCheck.setString(1, city);
            ResultSet resultSetCity = preparedStatementCityCheck.executeQuery();
            if (resultSetCity.first()) {
                city_id = resultSetCity.getLong("id");
            } else {
                isCityExists = false;
            }

            PreparedStatement preparedStatementWeatherTypeCheck = con.prepareStatement(sqlWeatherTypeCheck, Statement.RETURN_GENERATED_KEYS);
            preparedStatementWeatherTypeCheck.setString(1, type);
            ResultSet resultSetWeatherType = preparedStatementWeatherTypeCheck.executeQuery();
            if (resultSetWeatherType.first()) {
                weather_type_id = resultSetWeatherType.getLong("id");
            } else {
                isWeatherTypeExists = false;
            }

            if (!isCityExists) {
                CityJdbcService cityJdbcService = new CityJdbcService();
                cityJdbcService.create(city);
                city_id = preparedStatementCityCheck.executeQuery().getLong("id");
                System.out.println(city_id);
            }

            if (!isWeatherTypeExists) {
                WeatherTypeJdbcService weatherTypeJdbcService = new WeatherTypeJdbcService();
                weatherTypeJdbcService.create(type);
                weather_type_id = preparedStatementWeatherTypeCheck.executeQuery().getLong("id");
                System.out.println(weather_type_id);
            }
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, city_id);
            preparedStatement.setLong(2, weather_type_id);
            preparedStatement.setDouble(3, temperature);
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void jdbcCreate(String city, String type, double temperature, Timestamp timestamp) {
        transactionTemplate.execute(status -> {
            try {
                create(city, type, temperature, timestamp);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw new RuntimeException();
            }
            return null;
        });
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

    public void getById(Long id) {
        String sql = "SELECT * " + "FROM weather w " + "JOIN city c ON w.city_id = c.id " + "JOIN weathertype wt ON w.weather_type_id = wt.id " + "WHERE w.id = ?";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getLong("id") + " " + resultSet.getLong("city_id") + " " + resultSet.getString("city") + " " + resultSet.getLong("weather_type_id") + " " + resultSet.getString("type") + " " + resultSet.getFloat("temperature") + " " + resultSet.getTimestamp("timestamp"));
            } else {
                System.out.println("No Data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAll() {
        String sql = "SELECT * " + "FROM weather w " + "JOIN city c ON w.city_id = c.id " + "JOIN weathertype wt ON w.weather_type_id = wt.id ";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getLong("id") + " " + resultSet.getLong("city_id") + " " + resultSet.getString("city") + " " + resultSet.getLong("weather_type_id") + " " + resultSet.getString("type") + " " + resultSet.getFloat("temperature") + " " + resultSet.getTimestamp("timestamp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(long id, float temperature) {
        String sql = "UPDATE weather SET temperature = ? WHERE id = ?";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setFloat(1, temperature);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM weather WHERE id = (?)";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
