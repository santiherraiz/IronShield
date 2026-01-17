package main.java.org.DAO;

import main.java.org.BBDD.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class GuardiaDAO {

    public void insertarPosicion(int usuarioId, double latitud, double longitud) {

        String sql = "INSERT INTO guardia_posicion (usuario_id, latitud, longitud) VALUES (?, ?, ?)";

        try {
            ConexionBD bd = new ConexionBD();
            Connection conn = bd.conectar();

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, usuarioId);
            ps.setDouble(2, latitud);
            ps.setDouble(3, longitud);

            ps.executeUpdate();

            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}