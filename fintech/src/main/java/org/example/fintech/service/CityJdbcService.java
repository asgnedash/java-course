package org.example.fintech.service;

import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class CityJdbcService {

    public void create(String city) {
        String sql = "INSERT INTO city (city) VALUES (?)";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, city);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getById(Long id) {
        String sql = "SELECT * FROM city WHERE id = (?)";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getLong("id") + " " + resultSet.getString("city"));
            } else {
                System.out.println("No Data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAll() {
        String sql = "SELECT * FROM city";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getLong("id") + " " + resultSet.getString("city"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(long id, String city) {
        String sql = "UPDATE city SET city = ? WHERE id = ?";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, city);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM city WHERE id = (?)";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
