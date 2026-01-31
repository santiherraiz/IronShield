package org.BBDD;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConexionBDTest {

    @Test
    @DisplayName("Verificar que la conexión con la base de datos no es nula y se establece correctamente")
    void conectar() {

        ConexionBD db = new ConexionBD();


        Connection conn = db.conectar();

        // Comprobar que el objeto connection no sea null
        assertNotNull(conn, "La conexión falló: el objeto Connection es null. Revisa el archivo database.properties.");

        // Verificar el estado de la conexión y cerrarla
        try {
            if (conn != null) {
                // Comprobar que la conexión está abierta
                assertFalse(conn.isClosed(), "La conexión debería estar abierta.");

                conn.close();
            }
        } catch (SQLException e) {
            fail("Error al cerrar la conexión durante el test: " + e.getMessage());
        }
    }
}