package org.DTO;

/**
 * La clase {@link Agent} es una clase para almacenar los datos del agente.
 *<br>Esta clase tiene como atributos:
 *<br>  - <strong>Username - String</strong> Es el nombre o código de usuario
 *<br>  - <strong>Password - String</strong> Es la contraseña del usuario
 *<br>  - <strong>Latitud - double:</strong> La primera coordenada sirve para identificar la latitud del agente
 *<br>  - <strong>Longitud - double:</strong> La segunda coordenada sirve para identificar la longitud del agente
 */
public class Agent {
    public final String username;
    public final String pass;
    public final double latitude;
    public final double longitude;

    public Agent(String username) {
        this.username = username;
        this.pass = "";
        this.latitude = 0;
        this.longitude = 0;
    }

    public Agent(String username, String pass) {
        this.username = username;
        this.pass = pass;
        this.latitude = 0;
        this.longitude = 0;
    }

    public Agent(double latitude, double longitude) {
        this.username = "A";
        this.pass = "A2";
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