package org.example.connections;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlConnectionProvider {
    Connection getConnection() throws SQLException;
}