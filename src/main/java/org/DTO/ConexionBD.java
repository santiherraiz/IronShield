package org.DTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://gateway01.eu-central-1.prod.aws.tidbcloud.com:4000/monitorizacion?useSSL=true&requireSSL=true";
    private static final String USER = "2crvBDFQEEP8gwD.root";
    private static final String PASSWORD = "yvISQirrIXEb7Twc";

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}