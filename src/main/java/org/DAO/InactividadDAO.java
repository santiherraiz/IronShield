package org.DAO;

import org.BBDD.ConexionBD;
import org.Server.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para gestionar los registros de inactividad de los guardias.
 * Permite insertar alertas de inactividad y consultar el historial.
 */
public class InactividadDAO {

    /**
     * Clase interna para representar un registro de inactividad
     */
    public static class RegistroInactividad {
        public int id;
        public String username;
        public Timestamp fechaRegistro;
        
        public RegistroInactividad(int id, String username, Timestamp fechaRegistro) {
            this.id = id;
            this.username = username;
            this.fechaRegistro = fechaRegistro;
        }
        
        @Override
        public String toString() {
            return String.format("Inactividad[id=%d, username=%s, fecha=%s]", 
                id, username, fechaRegistro);
        }
    }

    /**
     * Inserta un registro de inactividad para un guardia.
     * Esta función se llama cuando se detecta que un guardia ha estado inactivo.
     * <p>
     * NOTA: Si tu tabla inactividad tiene la columna 'usuario_id' en lugar de 'username',
     * necesitarás modificar esta función o crear la columna username.
     *
     * @param username Nombre de usuario del guardia inactivo
     */
    public static void insertarInactividad(String username) {
        // Verificar si ya existe un registro reciente para evitar duplicados
        if (existeRegistroReciente(username, 60)) { // 60 segundos
            LogManager.info(String.format("Ya existe registro reciente de inactividad para %s, omitiendo...", username));
            return;
        }
        
        final String sql = "INSERT INTO inactividad (usuario_id, fecha) VALUES ((SELECT id FROM usuarios WHERE username = ?), NOW())";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new ConexionBD().conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            
            final int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                LogManager.warn(String.format("[ALERTA] Inactividad registrada para guardia %s", username));
            }
            
        } catch (SQLException e) {
            LogManager.error(String.format("Error al insertar inactividad para %s: %s", username, e.getMessage()));
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }

    }

    /**
     * Verifica si existe un registro de inactividad reciente para un guardia.
     * Esto evita crear múltiples alertas en un corto período de tiempo.
     * 
     * @param username Nombre de usuario del guardia
     * @param segundos Número de segundos para considerar como "reciente"
     * @return true si existe un registro reciente, false en caso contrario
     */
    private static boolean existeRegistroReciente(String username, int segundos) {
        String sql = "SELECT COUNT(*) as count " +
                     "FROM inactividad i " +
                     "JOIN usuarios u ON i.usuario_id = u.id " +
                     "WHERE u.username = ? " +
                     "AND i.fecha >= DATE_SUB(NOW(), INTERVAL ? SECOND)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setInt(2, segundos);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            
        } catch (SQLException e) {
            LogManager.error("Error al verificar registro reciente: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return false;
    }
}