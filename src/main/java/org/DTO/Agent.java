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
    public long date;

    // CODE 2
    public Agent(String username, double latitude, double longitude, Timestamp date) {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date.getTime();
    }

    @Override
    public String toString() {
        return  "Username: " + this.username + "\n" +
                "Pass: " + this.pass + "\n" +
                "Latitud: " + this.latitude + "\n" +
                "Longitud: " + this.longitude;
    }
}