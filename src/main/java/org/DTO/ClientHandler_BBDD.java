package org.DTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.PreparedStatement;

public class ClientHandler_BBDD implements Runnable {
    private Socket socket;

    public ClientHandler_BBDD(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String linea;
            while ((linea = in.readLine()) != null) {
                String[] datos = linea.split(";");
                String nombre = datos[0];
                double lat = Double.parseDouble(datos[1]);
                double lon = Double.parseDouble(datos[2]);
                guardarPosicion(nombre, lat, lon);
            }
        } catch (Exception e) {
            System.out.println("Cliente desconectado");
        }
    }

    private void guardarPosicion(String nombre, double lat, double lon) throws Exception {
        String sql = "INSERT INTO guardia_posicion (nombre_guardia, latitud, longitud) VALUES (?, ?, ?)";
        PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql);
        ps.setString(1, nombre);
        ps.setDouble(2, lat);
        ps.setDouble(3, lon);
        ps.executeUpdate();
    }
}