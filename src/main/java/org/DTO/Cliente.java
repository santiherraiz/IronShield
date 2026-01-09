package org.DTO;

import org.Server.UtilsServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {

        String host = "localhost";
        int puerto = UtilsServer.getServerPort();

        try (Socket socket = new Socket(host, puerto)) {
            System.out.println("--- MÓVIL CONECTADO AL SISTEMA IRON SHIELD ---");

            // Comunicacion
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Scanner sc = new Scanner(System.in);
            String mensajeUsuario;

            // Escribe en la consola y se envía al servidor
            System.out.println("Escribe coordenadas o mensaje (escribe 'salir' para terminar):");
            while (true) {
                System.out.print("> ");
                mensajeUsuario = sc.nextLine();

                if ("salir".equalsIgnoreCase(mensajeUsuario)) {
                    break;
                }


                salida.println(mensajeUsuario);

                String respuesta = entrada.readLine();
                System.out.println("SERVIDOR DICE: " + respuesta);
            }

        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}