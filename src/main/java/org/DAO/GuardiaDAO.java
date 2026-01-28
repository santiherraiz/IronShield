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

    public static void actualizarUbicacion(String username, double latitud, double longitud) {
        // Asumimos tabla 'usuarios' y columnas latitud/longitud existen como indicó el usuario.
        String sql = "UPDATE usuarios SET latitud = ?, longitud = ?, fecha = NOW() WHERE username = ?";

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, latitud);
            ps.setDouble(2, longitud);
            ps.setString(3, username);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static java.util.List<org.DTO.Agent> obtenerTodos() {
        java.util.List<org.DTO.Agent> agents = new java.util.ArrayList<>();
        String sql = "SELECT username, latitud, longitud, fecha FROM usuarios";

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                agents.add(new org.DTO.Agent(
                        rs.getString("username"),
                        rs.getDouble("latitud"),
                        rs.getDouble("longitud"),
                        rs.getTimestamp("fecha")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return agents;
    }
}