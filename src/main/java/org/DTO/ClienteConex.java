package org.DTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteConex implements Runnable {

    private Socket socket;

    public ClienteConex(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(
                        socket.getOutputStream(), true)
        ) {

            String mensaje;

            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("Datos recibidos: " + mensaje);

                salida.println("Datos recibidos correctamente");
            }

        } catch (Exception e) {
            System.out.println("Cliente desconectado");
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}