package org.DAO;


import org.BBDD.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class GuardiaDAO {

    public void insertarPosicion(int usuarioId, double latitud, double longitud) {
        String sql = "INSERT INTO guardia_posicion (usuario_id, latitud, longitud) VALUES (?, ?, ?)";

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            ps.setDouble(2, latitud);
            ps.setDouble(3, longitud);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}