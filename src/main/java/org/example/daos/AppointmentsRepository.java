package org.example.daos;

import org.example.connections.SqlConnectionProvider;
import org.example.daos.exceptions.PersistenceException;
import org.example.entities.Appointment;

import java.sql.*;
import java.util.*;

public class AppointmentsRepository implements EntityDao<Appointment, Long> {

    private final static String STORE_NEW_COMMAND = "INSERT INTO appointments (doctor_id, date_time, pacient_name) VALUES (?, ?, ?)";
    private final static String DELETE_COMMAND = "DELETE FROM appointments WHERE id = ?";
    private final static String UPDATE_COMMAND = "UPDATE appointments SET doctor_id = ?, date_time = ?, pacient_name = ? WHERE id = ?";
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

            stmt.setLong(1, newEntity.getDoctorId());
            stmt.setTimestamp(2, Timestamp.valueOf(newEntity.getDateTime()));
            stmt.setString(3, newEntity.getPacientName());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Couldn't add record");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                newEntity.setId(generatedKeys.getLong(1));
                return newEntity;
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to store new entity: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_COMMAND)) {

            stmt.setLong(1, id);

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to delete entity: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Long id, Appointment updatedEntity) {
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_COMMAND)) {

            stmt.setLong(1, updatedEntity.getDoctorId());
            stmt.setTimestamp(2, Timestamp.valueOf(updatedEntity.getDateTime()));
            stmt.setString(3, updatedEntity.getPacientName());
            stmt.setLong(4, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Failed to update entity: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Appointment> get(long id) {
        Optional<Appointment> result = Optional.empty();
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_BY_ID_QUERY)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getLong("id"));
                appointment.setDoctorId(rs.getLong("doctor_id"));
                appointment.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                appointment.setPacientName(rs.getString("pacient_name"));
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
                Appointment appointment = new Appointment();
                appointment.setId(rs.getLong("id"));
                appointment.setDoctorId(rs.getLong("doctor_id"));
                appointment.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                appointment.setPacientName(rs.getString("pacient_name"));
                results.add(appointment);
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to fetch multiple entities: " + e.getMessage(), e);
        }

        return results;
    }
}
