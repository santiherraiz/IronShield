package org.DTO;

/**
 * La clase {@link User} es una clase para almacenar los datos del agente.
 *<br>Esta clase tiene como atributos:
 *<br>  - <strong>Nombre - String:</strong> Sirve para identificar a cada agente
 *<br>  - <strong>Latitud - double:</strong> La primera coordenada sirve para identificar la latitud del agente
 *<br>  - <strong>Longitud - double:</strong> La segunda coordenada sirve para identificar la longitud del agente
 *<br>  Aquí también se lanza la instancía del agente, para que se conecte a la base de datos.
 */
public class User {
    private final String name;
    private final double latitude;
    private final double longitude;

    public User(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Se podría llevar a otro fichero, debatiendo si debería
    static void main() {
        User user = new User("Xavier", 41.3851, 2.1734);
        new Thread(new UserConn(user)).start();
    }

    @Override
    public String toString() {
        return "Nombre: " + this.name + "\n" +
                "Latitud: " + this.latitude + "\n" +
                "Longitud: " + this.longitude;
    }
}