package org.DTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {

    @Test
    @DisplayName("Verificar que el método toString genera la cadena de texto correctamente")
    void testToString() {
        Agent agente = new Agent("Guardia_Test", 40.0, -3.0, new java.sql.Timestamp(System.currentTimeMillis()));
        String resultado = agente.toString();

        assertNotNull(resultado);
        assertTrue(resultado.contains("Guardia_Test"));
    }
}