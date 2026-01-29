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
     * Este método se llama cuando se detecta que un guardia ha estado inactivo.
     * 
     * NOTA: Si tu tabla inactividad tiene la columna 'usuario_id' en lugar de 'username',
     * necesitarás modificar este método o crear la columna username.
     * 
     * @param username Nombre de usuario del guardia inactivo
     * @return true si se insertó correctamente, false en caso contrario
     */
    public static boolean insertarInactividad(String username) {
        // Verificar si ya existe un registro reciente para evitar duplicados
        if (existeRegistroReciente(username, 60)) { // 60 segundos
            LogManager.info(String.format("Ya existe registro reciente de inactividad para %s, omitiendo...", username));
            return false;
        }
        
        String sql = "INSERT INTO inactividad (usuario_id, fecha) VALUES ((SELECT id FROM usuarios WHERE username = ?), NOW())";
        
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new ConexionBD().conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                LogManager.warn(String.format("⚠️ ALERTA: Inactividad registrada para guardia %s", username));
                return true;
            }
            
        } catch (SQLException e) {
            LogManager.error(String.format("Error al insertar inactividad para %s: %s", username, e.getMessage()));
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return false;
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

    /**
     * Obtiene el historial completo de inactividades de un guardia.
     * 
     * @param username Nombre de usuario del guardia
     * @return Lista de registros de inactividad ordenados por fecha descendente
     */
    public static List<RegistroInactividad> obtenerHistorialInactividad(String username) {
        List<RegistroInactividad> historial = new ArrayList<>();
        
        String sql = "SELECT i.id, u.username, i.fecha " +
                     "FROM inactividad i " +
                     "JOIN usuarios u ON i.usuario_id = u.id " +
                     "WHERE u.username = ? " +
                     "ORDER BY i.fecha DESC";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                historial.add(new RegistroInactividad(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getTimestamp("fecha")
                ));
            }
            
            LogManager.info(String.format("Historial de inactividad recuperado para %s: %d registros", 
                username, historial.size()));
            
        } catch (SQLException e) {
            LogManager.error("Error al obtener historial de inactividad: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return historial;
    }

    /**
     * Cuenta las inactividades de un guardia en el día actual.
     * Útil para generar estadísticas diarias.
     * 
     * @param username Nombre de usuario del guardia
     * @return Número de inactividades registradas hoy
     */
    public static int contarInactividadesHoy(String username) {
        String sql = "SELECT COUNT(*) as count " +
                     "FROM inactividad i " +
                     "JOIN usuarios u ON i.usuario_id = u.id " +
                     "WHERE u.username = ? " +
                     "AND DATE(i.fecha) = CURDATE()";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt("count");
                LogManager.info(String.format("Guardia %s tiene %d inactividades hoy", username, count));
                return count;
            }
            
        } catch (SQLException e) {
            LogManager.error("Error al contar inactividades: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return 0;
    }

    /**
     * Cuenta las inactividades de un guardia en un rango de fechas.
     * 
     * @param username Nombre de usuario del guardia
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Número de inactividades en el rango
     */
    public static int contarInactividadesEnRango(String username, Timestamp fechaInicio, Timestamp fechaFin) {
        String sql = "SELECT COUNT(*) as count " +
                     "FROM inactividad i " +
                     "JOIN usuarios u ON i.usuario_id = u.id " +
                     "WHERE u.username = ? " +
                     "AND i.fecha BETWEEN ? AND ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setTimestamp(2, fechaInicio);
            ps.setTimestamp(3, fechaFin);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            
        } catch (SQLException e) {
            LogManager.error("Error al contar inactividades en rango: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return 0;
    }

    /**
     * Obtiene todos los registros de inactividad del día actual.
     * Útil para reportes diarios y estadísticas globales.
     * 
     * @return Lista de todos los registros de inactividad de hoy
     */
    public static List<RegistroInactividad> obtenerInactividadesHoy() {
        List<RegistroInactividad> registros = new ArrayList<>();
        
        String sql = "SELECT i.id, u.username, i.fecha " +
                     "FROM inactividad i " +
                     "JOIN usuarios u ON i.usuario_id = u.id " +
                     "WHERE DATE(i.fecha) = CURDATE() " +
                     "ORDER BY i.fecha DESC";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                registros.add(new RegistroInactividad(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getTimestamp("fecha")
                ));
            }
            
            LogManager.info(String.format("Inactividades de hoy recuperadas: %d registros", registros.size()));
            
        } catch (SQLException e) {
            LogManager.error("Error al obtener inactividades de hoy: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return registros;
    }

    /**
     * Obtiene estadísticas de inactividad por guardia para el día actual.
     * 
     * @return Lista de pares [username, count] ordenados por count descendente
     */
    public static List<String[]> obtenerEstadisticasHoy() {
        List<String[]> estadisticas = new ArrayList<>();
        
        String sql = "SELECT u.username, COUNT(*) as count " +
                     "FROM inactividad i " +
                     "JOIN usuarios u ON i.usuario_id = u.id " +
                     "WHERE DATE(i.fecha) = CURDATE() " +
                     "GROUP BY u.username " +
                     "ORDER BY count DESC";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                estadisticas.add(new String[] {
                    rs.getString("username"),
                    String.valueOf(rs.getInt("count"))
                });
            }
            
        } catch (SQLException e) {
            LogManager.error("Error al obtener estadísticas: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (ps != null) ps.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return estadisticas;
    }
}