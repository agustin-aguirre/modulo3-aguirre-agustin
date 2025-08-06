package org.example.daos;

import org.example.connections.SqlConnectionProvider;
import org.example.daos.exceptions.PersistenceException;
import org.example.entities.Appointment;

import java.sql.*;
import java.util.*;

public class AppointmentsRepository implements EntityDao<Appointment, Integer> {

    private final static String STORE_NEW_COMMAND = "INSERT INTO appointments (client_id, date_time) VALUES (?, ?)";
    private final static String DELETE_COMMAND = "DELETE FROM appointments WHERE id = ?";
    private final static String UPDATE_COMMAND = "UPDATE appointments SET client_id = ?, date_time = ? WHERE id = ?";
    private final static String GET_BY_ID_QUERY = "SELECT * FROM appointments WHERE id = ?";
    private final static String GET_ALL_QUERY = "SELECT * FROM appointments";

    private final SqlConnectionProvider connProvider;

    public AppointmentsRepository(SqlConnectionProvider connProvider) {
        this.connProvider = connProvider;
    }

    @Override
    public Appointment add(Appointment newEntity) {
        try (
            Connection conn = connProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(STORE_NEW_COMMAND, Statement.RETURN_GENERATED_KEYS)
        ) {

            stmt.setInt(1, newEntity.getClientId());
            stmt.setTimestamp(2, Timestamp.valueOf(newEntity.getDateTime()));

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Couldn't add record");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newEntity.setId(generatedKeys.getInt(1));
                    return newEntity;
                }
                else {
                    throw new SQLException("No Id returned.");
                }
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to store new entity: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_COMMAND)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to delete entity: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Appointment updatedEntity) {
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_COMMAND)) {

            stmt.setInt(1, updatedEntity.getClientId());
            stmt.setTimestamp(2, Timestamp.valueOf(updatedEntity.getDateTime()));
            stmt.setInt(3, updatedEntity.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Failed to update entity: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Appointment> get(Integer id) {
        Optional<Appointment> result = Optional.empty();
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_BY_ID_QUERY)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("id"),
                    rs.getInt("client_id"),
                    rs.getTimestamp("date_time").toLocalDateTime()
                );
                result = Optional.of(appointment);
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to fetch entity: " + e.getMessage(), e);
        }

        return result;
    }

    @Override
    public Collection<Appointment> getAll() {
        List<Appointment> results = new ArrayList<>();

        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_QUERY)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(new Appointment(
                    rs.getInt("id"),
                    rs.getInt("client_id"),
                    rs.getTimestamp("date_time").toLocalDateTime()
                ));
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to fetch multiple entities: " + e.getMessage(), e);
        }

        return results;
    }
}
