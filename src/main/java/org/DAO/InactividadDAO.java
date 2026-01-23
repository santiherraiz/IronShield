package org.DAO;

import org.BBDD.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class InactividadDAO {

    public void insertarInactividad(int usuarioId, int minutosInactivo) {
        String sql = "INSERT INTO inactividad (usuario_id, minutos_inactivo) VALUES (?, ?)";

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setInt(2, minutosInactivo);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}