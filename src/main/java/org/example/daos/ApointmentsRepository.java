package org.example.daos;

import org.example.connections.SqlConnectionProvider;
import org.example.entities.Apointment;
import org.example.entities.Doctor;

import java.sql.*;
import java.util.*;

public class ApointmentsRepository implements EntityDao<Apointment, Long> {

    private final static String STORE_NEW_COMMAND = "INSERT INTO apointments (doctor_id, date_time, pacient_name) VALUES (?, ?, ?)";
    private final static String DELETE_COMMAND = "DELETE FROM apointments WHERE id = ?";
    private final static String UPDATE_COMMAND = "UPDATE apointments SET doctor_id = ?, date_time = ?, pacient_name = ? WHERE id = ?";
    private final static String GET_BY_ID_QUERY = "SELECT * FROM apointments WHERE id = ?";
    private final static String GET_ALL_QUERY = "SELECT * FROM apointments";

    private final SqlConnectionProvider connProvider;

    public ApointmentsRepository(SqlConnectionProvider connProvider) {
        this.connProvider = connProvider;
    }


    @Override
    public void add(Apointment newEntity) {
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(STORE_NEW_COMMAND)) {

            stmt.setLong(1, newEntity.getDoctor().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(newEntity.getDateTime()));
            stmt.setString(3, newEntity.getPacientName());

            stmt.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public void update(Long id, Apointment updatedEntity) {
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_COMMAND)) {

            //private final static String UPDATE_COMMAND = "UPDATE apointments SET doctor_id = ?, date_time = ?, pacient_name = ? WHERE id = ?";
            stmt.setLong(1, updatedEntity.getDoctor().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(updatedEntity.getDateTime()));
            stmt.setString(3, updatedEntity.getPacientName());
            stmt.setLong(4, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Apointment> get(long id) {
        Optional<Apointment> result = Optional.empty();
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_QUERY)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Apointment apointment = new Apointment();
                apointment.setId(rs.getLong("id"));

                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("doctor_id"));
                apointment.setDoctor(doctor);

                apointment.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                apointment.setPacientName(rs.getString("pacient_name"));
                result = Optional.of(apointment);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public Collection<Apointment> getAll() {
        List<Apointment> results = new ArrayList<>();

        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_QUERY)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Apointment apointment = new Apointment();
                apointment.setId(rs.getLong("id"));

                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("doctor_id"));
                apointment.setDoctor(doctor);

                apointment.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
                apointment.setPacientName(rs.getString("pacient_name"));
                results.add(apointment);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}
