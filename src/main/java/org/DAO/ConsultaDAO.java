package main.java.org.DAO;

import main.java.org.BBDD.ConexionBD;

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

        try {
            ConexionBD bd = new ConexionBD();
            Connection conn = bd.conectar();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getDouble("latitud") + " , " +
                                rs.getDouble("longitud") + " - " +
                                rs.getTimestamp("fecha")
                );
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}