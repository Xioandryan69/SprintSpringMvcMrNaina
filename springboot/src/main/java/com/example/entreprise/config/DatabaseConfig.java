package com.example.entreprise.config;

import com.example.entreprise.database.DatabaseConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public DatabaseConnection databaseConnection(DataSource dataSource) {
        return new DatabaseConnection(dataSource);
    }
}