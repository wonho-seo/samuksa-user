package com.samuksa.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;

import static java.lang.Class.forName;
@WebAppConfiguration
public class TestDb {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/samuksa_user_db?&serverTimezone=UTC&autoReconnect=true&allowMultiQueries=true&characterEncoding=UTF-8";
    private static final String USER = "samuksa";
    private static final String PASSWORD = "Samuksa1!";

    @Test
    public void testConnection()throws Exception {
        Class.forName(DRIVER);
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
