package org.DTO;

/**
 * La clase {@link Agent} es una clase para almacenar los datos del agente.
 *<br>Esta clase tiene como atributos:
 *<br>  - <strong>Latitud - double:</strong> La primera coordenada sirve para identificar la latitud del agente
 *<br>  - <strong>Longitud - double:</strong> La segunda coordenada sirve para identificar la longitud del agente
 */
public class Agent {
    private final double latitude;
    private final double longitude;

    public Agent(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return  "Latitud: " + this.latitude + "\n" +
                "Longitud: " + this.longitude;
    }
}