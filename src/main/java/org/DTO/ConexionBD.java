package java.org.DTO;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/monitorizacion?useSSL=false&serverTimezone=UTC";
    private static final String USER = "guardia";
    private static final String PASS = "guardia123";

    public static Connection getConexion() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}