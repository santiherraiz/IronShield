package org;

import org.DTO.Hilo.ClientHandler;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        // Puerto original
        int puerto = 1234;
        int contadorIds = 0; // Contador para dar número de placa a los guardias

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("=== IRON SHIELD SERVER INICIADO ===");
            System.out.println("Esperando agentes en el puerto " + puerto + "...");

            while (true) {
                // 1. Aceptar conexión
                Socket clienteSocket = serverSocket.accept();
                contadorIds++; // Nuevo guardia conectado

                System.out.println(">> Nueva conexión entrante (Asignando ID: " + contadorIds + ")");

                // 2. Crear el hilo
                ClientHandler handler = new ClientHandler(clienteSocket, contadorIds);

                Thread hilo = new Thread(handler);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}