package org.DAO;

import org.BBDD.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class GuardiaDAO {

    public static void insertarPosicion(String username, double latitud, double longitud) {
        String sql = "INSERT INTO guardia_posicion (username, latitud, longitud) VALUES (?, ?, ?)";

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setDouble(2, latitud);
            ps.setDouble(3, longitud);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}