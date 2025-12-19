package org.DTO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int puerto = 12343;
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Esperando conexión en el puerto " + puerto + "...");

            while (true) {

                Socket clienteSocket = serverSocket.accept();


                ClienteConex handler = new ClienteConex(clienteSocket);
                Thread hilo = new Thread(handler);
                hilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}