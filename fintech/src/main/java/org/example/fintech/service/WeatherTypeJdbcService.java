package org.example.fintech.service;

import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class WeatherTypeJdbcService {
    public void create(String type) {
        String sql = "INSERT INTO weathertype (type) VALUES (?)";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, type);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getById(Long id) {
        String sql = "SELECT * FROM weathertype WHERE id = (?)";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getLong("id") + " " + resultSet.getString("type"));
            } else {
                System.out.println("No Data");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAll() {
        String sql = "SELECT * FROM weathertype";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getLong("id") + " " + resultSet.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(long id, String type) {
        String sql = "UPDATE weathertype SET type = ? WHERE id = ?";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, type);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM weathertype WHERE id = (?)";

        try (Connection con = DriverManager.getConnection("jdbc:h2:~/weather", "", "")) {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

