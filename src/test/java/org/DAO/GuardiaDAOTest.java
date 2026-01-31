package org.DAO;

import org.DTO.Agent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GuardiaDAOTest {

    @Test
    @DisplayName("Verificar que la lista de guardias activos no sea nula")
    void testObtenerTodos() {

        List<Agent> lista = GuardiaDAO.obtenerTodos();

        assertNotNull(lista, "El método obtenerTodos() nunca debe devolver null");
    }

    @Test
    @DisplayName("Verificar que la consulta de todos los guardias funciona sin errores")
    void testObtenerTodosLosGuardias() {
        // Validamos que el método termine su ejecución correctamente
        assertDoesNotThrow(() -> {
            GuardiaDAO.obtenerTodosLosGuardias();
        }, "La consulta SQL falló o hay un error de conexión");
    }
}