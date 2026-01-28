package org.DAO;

import org.BBDD.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InactividadDAO {

    public static void insertarInactividad(int usuarioId) {

        String sql = "INSERT INTO inactividad (usuario_id) VALUES (?)";

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}