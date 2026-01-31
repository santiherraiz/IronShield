package org.DAO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NombreDAOTest {

    @Test
    @DisplayName("Verificar que devuelve null cuando las credenciales no existen en la base de datos")
    void obtenerNombrePorUsuarioInvalido() {
        // Ejecutamos la consulta con datos que sabemos que no existen
        String nombre = NombreDAO.obtenerNombrePorUsuario("no_existe", "9999");

        // Comprobamos que el resultado sea null (comportamiento esperado)
        assertNull(nombre, "El nombre debería ser null para un usuario inexistente.");
    }

    @Test
    @DisplayName("Validar que el método no lanza excepciones al recibir parámetros vacíos")
    void obtenerNombreCamposVacios() {
        // Verificamos que la lógica del DAO gestione strings vacíos sin romperse
        assertDoesNotThrow(() -> {
            NombreDAO.obtenerNombrePorUsuario("", "");
        }, "El método debería manejar strings vacíos sin lanzar excepciones.");
    }
}