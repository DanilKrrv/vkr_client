package com.example.desktopclient.database;

import com.example.desktopclient.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";

        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String FIO = resultSet.getString("FIO");

                User user = new User();
                user.setId(id);
                user.setLogin(login);
                user.setPassword(password);
                user.setFIO(FIO);

                users.add(user);
            }
        }

        return users;
    }

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO user (login, password, FIO) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFIO());
            statement.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE user SET login = ?, password = ?, FIO = ? WHERE id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFIO());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        }
    }

    public void deleteUser(Long userId) throws SQLException {
        String query = "DELETE FROM user WHERE id = ?";

        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }
}