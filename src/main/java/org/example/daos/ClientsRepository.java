package org.example.daos;

import org.example.connections.SqlConnectionProvider;
import org.example.daos.exceptions.PersistenceException;
import org.example.entities.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ClientsRepository implements EntityDao<Client, Integer> {

    private final static String STORE_NEW_COMMAND = "INSERT INTO clients (name) VALUES (?)";
    private final static String DELETE_COMMAND = "DELETE FROM clients WHERE id = ?";
    private final static String UPDATE_COMMAND = "UPDATE clients SET name = ? WHERE id = ?";
    private final static String GET_BY_ID_QUERY = "SELECT * FROM clients WHERE id = ?";
    private final static String GET_ALL_QUERY = "SELECT * FROM clients";

    private final SqlConnectionProvider connProvider;

    public ClientsRepository(SqlConnectionProvider connProvider) {
        this.connProvider = connProvider;
    }

    @Override
    public Client add(Client newEntity) {
        try (
            Connection conn = connProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(STORE_NEW_COMMAND, Statement.RETURN_GENERATED_KEYS)
        ) {

            stmt.setString(1, newEntity.getName());

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
    public void update(Client updatedEntity) {
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_COMMAND)) {

            stmt.setString(1, updatedEntity.getName());
            stmt.setInt(2, updatedEntity.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenceException("Failed to update entity: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Client> get(Integer id) {
        Optional<Client> result = Optional.empty();
        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_BY_ID_QUERY)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("name")
                );
                result = Optional.of(client);
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to fetch entity: " + e.getMessage(), e);
        }

        return result;
    }

    @Override
    public Collection<Client> getAll() {
        List<Client> results = new ArrayList<>();

        try (Connection conn = connProvider.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_QUERY)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(new Client(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        }
        catch (SQLException e) {
            throw new PersistenceException("Failed to fetch multiple entities: " + e.getMessage(), e);
        }

        return results;
    }
}
