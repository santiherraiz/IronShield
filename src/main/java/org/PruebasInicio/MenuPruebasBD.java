package org.PruebasInicio;

import org.BBDD.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class MenuPruebasBD {

    public static void main(String[] args) {

        ConexionBD conexionBD = new ConexionBD();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- MENU PRUEBAS BBDD ---");
            System.out.println("1. Insertar usuario de prueba (Guardia o Supervisor)");
            System.out.println("2. Insertar posición de guardia (Se hace al dar el botón)");
            System.out.println("3. Ver usuarios");
            System.out.println("4. Ver posiciones");
            System.out.println("5. Ver inactividades");
            System.out.println("0. Salir");
            System.out.print("Opción: ");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> insertarUsuario(conexionBD);
                case 2 -> insertarPosicion(conexionBD);
                case 3 -> verUsuarios(conexionBD);
                case 4 -> verPosiciones(conexionBD);
                case 5 -> verInactividades(conexionBD);
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida");
            }

        } while (opcion != 0);
    }


    private static void insertarUsuario(ConexionBD bd) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String pass = sc.nextLine();

        System.out.print("Rol (GUARDIA / SUPERVISOR): ");
        String rol = sc.nextLine();
                                            //Rol tiene que ser GUARDIA o SUPERVISOR, si no, no va a insertar
        String sql = "INSERT INTO usuarios (nombre, username, pass, rol) VALUES (?, ?, ?, ?)";

        try (Connection con = bd.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, username);
            ps.setString(3, pass);
            ps.setString(4, rol);

            ps.executeUpdate();
            System.out.println("Usuario insertado correctamente");

        } catch (Exception e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
    }

    private static void insertarPosicion(ConexionBD bd) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Username del guardia: ");
        String username = sc.nextLine();

        System.out.print("Latitud: ");
        double latitud = sc.nextDouble();

        System.out.print("Longitud: ");
        double longitud = sc.nextDouble();

        String sql = "INSERT INTO guardia_posicion (username, latitud, longitud) VALUES (?, ?, ?)";

        try (Connection con = bd.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setDouble(2, latitud);
            ps.setDouble(3, longitud);

            ps.executeUpdate();
            System.out.println("Posición insertada correctamente");

        } catch (Exception e) {
            System.out.println("Error al insertar posición: " + e.getMessage());
        }
    }


    //Inactividad se hace ya automaticamente, por eso no he incluido un insert



    private static void verUsuarios(ConexionBD bd) {
        String sql = "SELECT * FROM usuarios";
        ejecutarSelect(bd, sql);
    }

    private static void verPosiciones(ConexionBD bd) {
        String sql = "SELECT * FROM guardia_posicion";
        ejecutarSelect(bd, sql);
    }

    private static void verInactividades(ConexionBD bd) {
        String sql = "SELECT * FROM inactividad";
        ejecutarSelect(bd, sql);
    }

    private static void ejecutarSelect(ConexionBD bd, String sql) {
        try (Connection con = bd.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int columnas = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnas; i++) {
                    System.out.print(rs.getString(i) + " | ");
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}