package org.example.fintech.service;

import org.example.fintech.model.Weather;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class WeatherJdbcService {

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
            if (resultSetCity.next()) {
                city_id = resultSetCity.getLong("id");
            } else {
                isCityExists = false;
            }

            PreparedStatement preparedStatementWeatherTypeCheck = con.prepareStatement(sqlWeatherTypeCheck, Statement.RETURN_GENERATED_KEYS);
            preparedStatementWeatherTypeCheck.setString(1, type);
            ResultSet resultSetWeatherType = preparedStatementWeatherTypeCheck.executeQuery();
            if (resultSetWeatherType.next()) {
                weather_type_id = resultSetWeatherType.getLong("id");
            } else {
                isWeatherTypeExists = false;
            }

            if (!isCityExists) {
                CityJdbcService cityJdbcService = new CityJdbcService();
                cityJdbcService.create(city);
            }

            if (!isWeatherTypeExists) {
                WeatherTypeJdbcService weatherTypeJdbcService = new WeatherTypeJdbcService();
                weatherTypeJdbcService.create(type);
            }
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, city_id);
            preparedStatement.setLong(2, weather_type_id);
            preparedStatement.setDouble(3, temperature);
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.executeUpdate();
            /*
            if (isCityExists && isWeatherTypeExists) {
                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setLong(1, city_id);
                preparedStatement.setLong(2, weather_type_id);
                preparedStatement.setFloat(3, temperature);
                preparedStatement.setTimestamp(4, timestamp);
                preparedStatement.executeUpdate();
            } else {
                System.out.println("Incorrect city and/or weather type");
            }
             */


        } catch (SQLException e) {
            e.printStackTrace();
        }


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
