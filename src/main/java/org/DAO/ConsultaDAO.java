package org.DAO;

import org.BBDD.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConsultaDAO {

    public void obtenerPosiciones(int usuarioId) {
        String sql = """
            SELECT latitud, longitud, fecha
            FROM guardia_posicion
            WHERE usuario_id = ?
            ORDER BY fecha DESC
        """;

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println(
                            rs.getDouble("latitud") + " , " +
                                    rs.getDouble("longitud")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}