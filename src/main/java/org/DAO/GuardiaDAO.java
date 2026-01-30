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
     * Esta función inserta un nuevo registro en guardia_posicion con la ubicación actual.
     * 
     * @param username Nombre de usuario del guardia
     * @param lat Latitud
     * @param lon Longitud
     */
    public static void actualizarUbicacion(String username, double lat, double lon) {
        Connection conn;
        PreparedStatement pstmtPosicion;

        try {
            conn = new ConexionBD().conectar();

            String sqlPosicion = "INSERT INTO guardia_posicion (username, latitud, longitud, fecha) VALUES (?, ?, ?, NOW())";
            pstmtPosicion = conn.prepareStatement(sqlPosicion);
            pstmtPosicion.setString(1, username);
            pstmtPosicion.setDouble(2, lat);
            pstmtPosicion.setDouble(3, lon);
            pstmtPosicion.executeUpdate();

            pstmtPosicion.close();
            conn.close();
        } catch (Exception e) {
            LogManager.error("Error al actualizar ubicación: " + e.getMessage());
        }
    }

    /**
     * Obtiene SOLO los guardias que están ACTIVOS (han enviado señal recientemente).
     * Un guardia se considera activo si su último registro es menor al umbral configurado.
     * <p>
     * IMPORTANTE: Esta función filtra en el BACKEND, no en el frontend.
     * 
     * @return Lista de agentes activos con su última posición
     */
    public static List<Agent> obtenerTodos() {
        final List<Agent> agentes = new ArrayList<>();
        
        // Query que devuelve SOLO guardias activos (últimos N segundos según configuración)
        String sql = "SELECT t1.username, t1.latitud, t1.longitud, t1.fecha " +
                     "FROM guardia_posicion t1 " +
                     "WHERE t1.fecha = (SELECT MAX(t2.fecha) FROM guardia_posicion t2 WHERE t2.username = t1.username) " +
                     "AND t1.fecha >= DATE_SUB(NOW(), INTERVAL ? SECOND)";

        Connection conn;
        PreparedStatement pstmt;

        try {
            conn = new ConexionBD().conectar();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, AppConfig.INACTIVITY_THRESHOLD_SECONDS);

            addGuardsToList(agentes, conn, pstmt);
        } catch (Exception e) {
            LogManager.error("Error al obtener guardias activos: " + e.getMessage());
        }
        
        return agentes;
    }

    private static void addGuardsToList(List<Agent> agentes, Connection conn, PreparedStatement pstmt) throws SQLException {
        ResultSet rs;
        rs = pstmt.executeQuery();

        while (rs.next()) {
            agentes.add(new Agent(
                    rs.getString("username"),
                    rs.getDouble("latitud"),
                    rs.getDouble("longitud"),
                    rs.getTimestamp("fecha")
            ));
        }

        rs.close();
        pstmt.close();
        conn.close();
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

        Connection conn;
        PreparedStatement pstmt;

        try {
            conn = new ConexionBD().conectar();
            pstmt = conn.prepareStatement(sql);
            addGuardsToList(agentes, conn, pstmt);
        } catch (Exception e) {
            LogManager.error("Error al obtener todos los guardias: " + e.getMessage());
        }
        
        return agentes;
    }
    
    /**
     * Función de compatibilidad con versiones anteriores.
     * Redirige a actualizarUbicacion.
     */
    public static void insertarPosicion(String username, double latitud, double longitud) {
        actualizarUbicacion(username, latitud, longitud);
    }
}