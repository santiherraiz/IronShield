package org.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/     ";
    private static final String USER = "";
    private static final String PASS = "";

    public static Connection obtenerConexion() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASS);
    }
}