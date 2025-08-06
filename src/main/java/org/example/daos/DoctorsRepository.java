package org.example.daos;

import org.example.connections.SqlConnectionProvider;
import org.example.daos.exceptions.PersistenceException;
import org.example.entities.Doctor;

import java.sql.*;
import java.util.*;

public class DoctorsRepository implements EntityDao<Doctor, Long> {

    private final static String STORE_NEW_COMMAND = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";
    private final static String DELETE_COMMAND = "DELETE FROM doctors WHERE id = ?";
    private final static String UPDATE_COMMAND = "UPDATE doctors SET name = ?, specialization = ? WHERE id = ?";
    private final static String GET_BY_ID_QUERY = "SELECT * FROM doctors WHERE id = ?";
    private final static String GET_ALL_QUERY = "SELECT * FROM doctors";

    private final SqlConnectionProvider connProvider;

    public DoctorsRepository(SqlConnectionProvider connProvider) {
        this.connProvider = connProvider;
    }

    @Override
    public Doctor add(Doctor newEntity) {
        try (
            Connection conn = connProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(STORE_NEW_COMMAND, Statement.RETURN_GENERATED_KEYS)
        ) {

            stmt.setString(1, newEntity.getName());
            stmt.setString(2, newEntity.getSpecialization());

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
    public void update(Long id, Doctor updatedEntity) {
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_COMMAND)) {

            stmt.setString(1, updatedEntity.getName());
            stmt.setString(2, updatedEntity.getSpecialization());
            stmt.setLong(3, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Failed to update entity: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Doctor> get(long id) {
        Optional<Doctor> result = Optional.empty();
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_BY_ID_QUERY)) {

            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("id"));
                doctor.setName(rs.getString("name"));
                doctor.setSpecialization(rs.getString("specialization"));
                result = Optional.of(doctor);
            }

        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to fetch entity: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public Collection<Doctor> getAll() {
        List<Doctor> results = new ArrayList<>();

        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_QUERY)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getLong("id"));
                doctor.setName(rs.getString("name"));
                doctor.setSpecialization(rs.getString("specialization"));
                results.add(doctor);
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to fetch multiple entities: " + e.getMessage(), e);
        }

        return results;
    }
}
