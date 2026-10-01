package com.example.entreprise.database;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private final DataSource connection;

    public DatabaseConnection(DataSource connection) {
        this.connection = connection;
    }

    public Connection getConnection() throws SQLException {
        return DataSourceUtils.getConnection(connection);
    }
}