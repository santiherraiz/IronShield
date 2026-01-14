package org.DTO;


import org.Server.UtilsServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = UtilsServer.getServerPort();
    private static final String HOST = UtilsServer.getServerName();

    public static void main(String[] args) {

        System.out.println("Cargando configuración Iron Shield...");

        int contadorIds = 0; // Saber quien es el cliente

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("=== IRON SHIELD SERVER INICIADO ===");
            System.out.println("Configuración cargada: " + HOST + ":" + PORT);
            System.out.println("Esperando agentes...");

            while (true) {

                Socket clienteSocket = serverSocket.accept();
                contadorIds++;

                System.out.println(">> Nueva conexión entrante (Asignando ID: " + contadorIds + ")");

                ClienteConex handler = new ClienteConex(clienteSocket, contadorIds);


                Thread hilo = new Thread(handler);
                hilo.start();
            }
        } catch (IOException e) {
            System.err.println("Error crítico en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}