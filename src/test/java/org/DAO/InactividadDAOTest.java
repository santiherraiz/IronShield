package org.DAO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InactividadDAOTest {

    @Test
    @DisplayName("Verificar que la detección de registros recientes no lance excepciones")
    void testExisteRegistroReciente() {
        // Probamos con un usuario que probablemente no tenga inactividades recientes
        // El objetivo es validar que la consulta SQL sea sintácticamente correcta
        assertDoesNotThrow(() -> {
            InactividadDAO.insertarInactividad("USUARIO_INEXISTENTE_TEST");
        }, "La lógica de verificación de registros recientes falló");
    }

    @Test
    @DisplayName("Validar que el sistema maneja correctamente nombres de usuario nulos")
    void testInsercionConNulo() {
        // Verificamos que el DAO no explote (NullPointerException) si recibe un nulo
        // El código debería capturar el error o simplemente no encontrar el registro
        assertDoesNotThrow(() -> {
            InactividadDAO.insertarInactividad(null);
        }, "El método debería gestionar el parámetro null sin romper el hilo");
    }
}