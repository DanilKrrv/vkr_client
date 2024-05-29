package com.example.desktopclient.database;

import com.example.desktopclient.model.Attestation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttestationDAO {
    private static final String INSERT_SQL = "INSERT INTO attestation (title, isValid, dataPassed, user_id) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_USER_ID_SQL = "SELECT * FROM attestation WHERE user_id = ?";
    private static final String DELETE_SQL = "DELETE FROM attestation WHERE id = ?";
    private static final String UPDATE_SQL = "UPDATE attestation SET title = ?, isValid = ?, dataPassed = ? WHERE id = ?";

    public void addAttestation(Attestation attestation) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
            statement.setString(1, attestation.getTitle());
            statement.setBoolean(2, attestation.getIsValid());
            statement.setString(3, attestation.getDataPassed());
            statement.setLong(4, attestation.getUserId());
            statement.executeUpdate();
        }
    }

    public List<Attestation> getAttestationsByUserId(Long userId) throws SQLException {
        List<Attestation> attestations = new ArrayList<>();
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_USER_ID_SQL)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Attestation attestation = new Attestation();
                attestation.setId(resultSet.getLong("id"));
                attestation.setTitle(resultSet.getString("title"));
                attestation.setIsValid(resultSet.getBoolean("isValid"));
                attestation.setDataPassed(resultSet.getString("dataPassed"));
                attestation.setUserId(resultSet.getLong("user_id"));
                attestations.add(attestation);
            }
        }
        return attestations;
    }

    public void deleteAttestation(Long id) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public void updateAttestation(Attestation attestation) throws SQLException {
        try (Connection connection = DatabaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, attestation.getTitle());
            statement.setBoolean(2, attestation.getIsValid());
            statement.setString(3, attestation.getDataPassed());
            statement.setLong(4, attestation.getId());
            statement.executeUpdate();
        }
    }
}
