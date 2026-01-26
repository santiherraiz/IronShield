package org.DTO;

import java.sql.Timestamp;

/**
 * La clase {@link Agent} es una clase para almacenar los datos del agente.
 *<br>Esta clase tiene como atributos:
 *<br>  - <strong>Username - String</strong> Es el nombre o código de usuario
 *<br>  - <strong>Password - String</strong> Es la contraseña del usuario
 *<br>  - <strong>Latitud - double:</strong> La primera coordenada sirve para identificar la latitud del agente
 *<br>  - <strong>Longitud - double:</strong> La segunda coordenada sirve para identificar la longitud del agente
 */
public class Agent {
    public int code;
    public String username;
    public String pass;
    public double latitude;
    public double longitude;
    public Timestamp fecha;

    // CODE 1
    public Agent(String username, int code) {
        this.code = code;
        this.username = username;
    }

    // CODE 2
    public Agent(String username, String pass, int code) {
        this.code = code;
        this.username = username;
        this.pass = pass;
    }

    // CODE 3
    public Agent(String username, String pass, double latitude, double longitude, int code) {
        this.code = code;
        this.pass = pass;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // CODE 4
    public Agent(double latitude, double longitude, int code) {
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // CODE 5
    public Agent(String username, String pass, double latitude, double longitude, Timestamp fecha, int code) {
        this.code = code;
        this.username = username;
        this.pass = pass;
        this.fecha = fecha;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return  "Username: " + this.username + "\n" +
                "Pass: " + this.pass + "\n" +
                "Latitud: " + this.latitude + "\n" +
                "Longitud: " + this.longitude;
    }
}