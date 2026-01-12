// src/main/java/com/project/configuration/DatabaseConfigProperties.java
package com.project.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "database.connection")
public class DatabaseConfigProperties {

    private String host;
    private int port;
    private String username;
    private String password;
    private String database;

    // Getters et setters
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return "DatabaseConfigProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", database='" + database + '\'' +
                '}';
    }
}
