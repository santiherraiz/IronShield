package org.DAO;

import org.BBDD.ConexionBD;
import org.Config.AppConfig;
import org.DTO.Agent;
import org.Server.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GuardiaDAO {

    /**
     * Actualiza la ubicación de un guardia cuando pulsa el botón.
     * Este método inserta un nuevo registro en guardia_posicion con la ubicación actual.
     * 
     * @param username Nombre de usuario del guardia
     * @param lat Latitud
     * @param lon Longitud
     */
    public static void actualizarUbicacion(String username, double lat, double lon) {
        Connection conn = null;
        PreparedStatement pstmtPosicion = null;

        try {
            conn = new ConexionBD().conectar();

            String sqlPosicion = "INSERT INTO guardia_posicion (username, latitud, longitud, fecha) VALUES (?, ?, ?, NOW())";
            pstmtPosicion = conn.prepareStatement(sqlPosicion);
            pstmtPosicion.setString(1, username);
            pstmtPosicion.setDouble(2, lat);
            pstmtPosicion.setDouble(3, lon);
            pstmtPosicion.executeUpdate();

            LogManager.info(String.format("Nueva posición registrada para %s: (%.6f, %.6f)", 
                username, lat, lon));

        } catch (Exception e) {
            LogManager.error("Error al actualizar ubicación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { 
                if (pstmtPosicion != null) pstmtPosicion.close(); 
            } catch (SQLException e) {
                LogManager.error("Error al cerrar PreparedStatement: " + e.getMessage());
            }
            try { 
                if (conn != null) conn.close(); 
            } catch (SQLException e) {
                LogManager.error("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene SOLO los guardias que están ACTIVOS (han enviado señal recientemente).
     * Un guardia se considera activo si su último registro es menor al umbral configurado.
     * 
     * IMPORTANTE: Este método filtra en el BACKEND, no en el frontend.
     * 
     * @return Lista de agentes activos con su última posición
     */
    public static List<Agent> obtenerTodos() {
        List<Agent> agentes = new ArrayList<>();
        
        // Query que devuelve SOLO guardias activos (últimos N segundos según configuración)
        String sql = "SELECT t1.username, t1.latitud, t1.longitud, t1.fecha " +
                     "FROM guardia_posicion t1 " +
                     "WHERE t1.fecha = (SELECT MAX(t2.fecha) FROM guardia_posicion t2 WHERE t2.username = t1.username) " +
                     "AND t1.fecha >= DATE_SUB(NOW(), INTERVAL ? SECOND)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, AppConfig.INACTIVITY_THRESHOLD_SECONDS);
            
            rs = pstmt.executeQuery();

            while (rs.next()) {
                agentes.add(new Agent(
                        rs.getString("username"),
                        rs.getDouble("latitud"),
                        rs.getDouble("longitud"),
                        rs.getTimestamp("fecha")
                ));
            }
            
            LogManager.info(String.format("Guardias activos recuperados: %d", agentes.size()));
            
        } catch (Exception e) {
            LogManager.error("Error al obtener guardias activos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return agentes;
    }

    /**
     * Obtiene TODOS los guardias que han enviado al menos una posición,
     * independientemente de cuándo fue su última actualización.
     *
     * @return Lista de todos los agentes con su última posición conocida
     */
    public static List<Agent> obtenerTodosLosGuardias() {
        List<Agent> agentes = new ArrayList<>();
        
        // Query que obtiene el último registro de CADA guardia, sin filtro temporal
        String sql = "SELECT t1.username, t1.latitud, t1.longitud, t1.fecha " +
                     "FROM guardia_posicion t1 " +
                     "WHERE t1.fecha = (SELECT MAX(t2.fecha) FROM guardia_posicion t2 WHERE t2.username = t1.username)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                agentes.add(new Agent(
                        rs.getString("username"),
                        rs.getDouble("latitud"),
                        rs.getDouble("longitud"),
                        rs.getTimestamp("fecha")
                ));
            }
            
            LogManager.info(String.format("Total de guardias recuperados: %d", agentes.size()));
            
        } catch (Exception e) {
            LogManager.error("Error al obtener todos los guardias: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return agentes;
    }

    /**
     * Obtiene los guardias que están INACTIVOS.
     * 
     * @return Lista de agentes inactivos
     */
    public static List<Agent> obtenerInactivos() {
        List<Agent> todosLosGuardias = obtenerTodosLosGuardias();
        List<Agent> activos = obtenerTodos();
        
        // Filtrar los que NO están en la lista de activos
        List<Agent> inactivos = new ArrayList<>();
        for (Agent guardia : todosLosGuardias) {
            boolean estaActivo = activos.stream()
                .anyMatch(a -> a.username.equals(guardia.username));
            
            if (!estaActivo) {
                inactivos.add(guardia);
            }
        }
        
        LogManager.info(String.format("Guardias inactivos detectados: %d", inactivos.size()));
        
        return inactivos;
    }

    /**
     * Obtiene el historial de posiciones de un guardia específico.
     */
    public static List<Agent> obtenerHistorial(String username, int limit) {
        List<Agent> historial = new ArrayList<>();
        
        String sql = "SELECT username, latitud, longitud, fecha " +
                     "FROM guardia_posicion " +
                     "WHERE username = ? " +
                     "ORDER BY fecha DESC " +
                     "LIMIT ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setInt(2, limit);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                historial.add(new Agent(
                        rs.getString("username"),
                        rs.getDouble("latitud"),
                        rs.getDouble("longitud"),
                        rs.getTimestamp("fecha")
                ));
            }
            
        } catch (Exception e) {
            LogManager.error("Error al obtener historial: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return historial;
    }

    /**
     * Obtiene la última posición conocida de un guardia específico.
     */
    public static Agent obtenerUltimaPosicion(String username) {
        String sql = "SELECT username, latitud, longitud, fecha " +
                     "FROM guardia_posicion " +
                     "WHERE username = ? " +
                     "ORDER BY fecha DESC " +
                     "LIMIT 1";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Agent(
                        rs.getString("username"),
                        rs.getDouble("latitud"),
                        rs.getDouble("longitud"),
                        rs.getTimestamp("fecha")
                );
            }
            
        } catch (Exception e) {
            LogManager.error("Error al obtener última posición: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return null;
    }

    /**
     * Verifica si un guardia específico está activo.
     */
    public static boolean estaActivo(String username) {
        String sql = "SELECT COUNT(*) as count " +
                     "FROM guardia_posicion " +
                     "WHERE username = ? " +
                     "AND fecha >= DATE_SUB(NOW(), INTERVAL ? SECOND) " +
                     "AND fecha = (SELECT MAX(fecha) FROM guardia_posicion WHERE username = ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = new ConexionBD().conectar();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setInt(2, AppConfig.INACTIVITY_THRESHOLD_SECONDS);
            pstmt.setString(3, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            
        } catch (Exception e) {
            LogManager.error("Error al verificar estado activo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) {}
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        
        return false;
    }
    
    /**
     * Método de compatibilidad con versiones anteriores.
     * Redirige a actualizarUbicacion.
     */
    public static void insertarPosicion(String username, double latitud, double longitud) {
        actualizarUbicacion(username, latitud, longitud);
    }
}