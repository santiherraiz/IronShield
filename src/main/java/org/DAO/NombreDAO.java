package org.DAO;

import org.BBDD.ConexionBD;
import org.Server.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NombreDAO {
    public static String obtenerNombrePorUsuario(String username, String pass) {
        String sql = "SELECT nombre FROM usuarios WHERE username = ? AND pass = ?";
        String nombre = null;

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nombre = rs.getString("nombre");
                }
            }
        } catch (Exception e) {
            LogManager.error("[ERROR] Ha habido un error en la consulta NombreDAO", e);
        }
        return nombre;
    }
}
