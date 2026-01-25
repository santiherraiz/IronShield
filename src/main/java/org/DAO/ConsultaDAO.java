package org.DAO;

import org.BBDD.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    public static List<String> obtenerAlertas() {

        List<String> alertas = new ArrayList<>();

        String sql = """
            SELECT u.username, gp.latitud, gp.longitud, i.fecha
            FROM inactividad i
            JOIN usuarios u ON i.usuario_id = u.id
            JOIN guardia_posicion gp ON gp.username = u.username
            ORDER BY i.fecha DESC
        """;
            //Si lo quieres ordenado por fecha vale, si no lo quitas

        try (Connection conn = new ConexionBD().conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String fila = rs.getString("username") + ";" +
                        rs.getDouble("latitud") + ";" +
                        rs.getDouble("longitud") + ";" +
                        rs.getTimestamp("fecha");
                alertas.add(fila);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return alertas;
    }

/*  Uso ejemplo pal xavi

    List<String> alertas = InactividadDAO.obtenerAlertas();

for (String alerta : alertas) {
        String[] datos = alerta.split(";");
        String username = datos[0];
        double lat = Double.parseDouble(datos[1]);
        double lon = Double.parseDouble(datos[2]);
        String fecha = datos[3];
*/


}