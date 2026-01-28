package org.DAO;

import org.BBDD.ConexionBD;
import org.DTO.Agent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GuardiaDAO {

    // Método llamado cuando el Guardia pulsa el botón
    public static void actualizarUbicacion(String username, double lat, double lon) {
        Connection conn = null;
        PreparedStatement pstmtGuardia = null;
        PreparedStatement pstmtPosicion = null;

        try {
            conn = new ConexionBD().conectar();
            // Desactivamos autocommit para hacer las dos cosas a la vez (Transacción)
            conn.setAutoCommit(false);

            // 1. Actualizamos la FECHA en la tabla de usuarios
            String sqlGuardia = "UPDATE guardia_posicion SET fecha = NOW() WHERE username = ?";
            pstmtGuardia = conn.prepareStatement(sqlGuardia);
            pstmtGuardia.setString(1, username);
            pstmtGuardia.executeUpdate();

            // 2. Insertamos la posición en la tabla de historial
            // Usamos 'username' y 'latitud'/'longitud' como en el archivo original
            String sqlPosicion = "INSERT INTO guardia_posicion (username, latitud, longitud) VALUES (?, ?, ?)";
            pstmtPosicion = conn.prepareStatement(sqlPosicion);
            pstmtPosicion.setString(1, username);
            pstmtPosicion.setDouble(2, lat);
            pstmtPosicion.setDouble(3, lon);
            pstmtPosicion.executeUpdate();

            conn.commit(); // Confirmar cambios
            System.out.println("[DB] Guardia " + username + " actualizado correctamente.");

        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            try { if (pstmtGuardia != null) pstmtGuardia.close(); } catch (Exception e) {}
            try { if (pstmtPosicion != null) pstmtPosicion.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }

    // Método llamado por el Supervisor para ver la lista
    public static List<Agent> obtenerTodos() {
        List<Agent> agentes = new ArrayList<>();
        // Obtenemos username y fecha de usuarios
        String sql = "SELECT username, fecha FROM guardia_posicion";

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                Timestamp fecha = rs.getTimestamp("fecha");

                // Usamos el constructor Agent(String username, double latitude, double longitude, Timestamp date)
                // Ponemos lat/lon a 0 porque no las estamos leyendo de esta tabla
                agentes.add(new Agent(username, 0.0, 0.0, fecha));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return agentes;
    }
    
    // Mantenemos el método antiguo por compatibilidad si fuese necesario, pero redirigiendo
    public static void insertarPosicion(String username, double latitud, double longitud) {
        actualizarUbicacion(username, latitud, longitud);
    }
}