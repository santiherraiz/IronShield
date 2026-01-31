package org.DTO;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {

    @Test
    @DisplayName("Verificar que el JSON de Login se convierte correctamente en un objeto Agent")
    void testDeserializacionLogin() {
        // Simulamos el mensaje que enviaría la app React
        String jsonInput = "{\"code\":1, \"username\":\"G001\", \"pass\":\"1234\"}";

        // Usamos Gson para ver si la estructura coincide con la clase Agent
        Agent agent = new Gson().fromJson(jsonInput, Agent.class);

        assertNotNull(agent);
        assertEquals(1, agent.code);
        assertEquals("G001", agent.username);
        assertEquals("1234", agent.pass);
    }

    @Test
    @DisplayName("Verificar que el JSON de Ubicación mantiene la precisión de las coordenadas")
    void testDeserializacionUbicacion() {
        // Simulamos el envío de coordenadas GPS
        String jsonInput = "{\"code\":3, \"username\":\"G001\", \"latitude\":40.4167, \"longitude\":-3.7037}";

        Agent agent = new Gson().fromJson(jsonInput, Agent.class);

        assertNotNull(agent);
        assertEquals(3, agent.code);
        assertEquals(40.4167, agent.latitude);
        assertEquals(-3.7037, agent.longitude);
    }

    @Test
    @DisplayName("Validar comportamiento ante un JSON incompleto")
    void testJsonInvalido() {
        String jsonInvalido = "{\"mensaje\": \"esto no es un agente\"}";

        Agent agent = new Gson().fromJson(jsonInvalido, Agent.class);

        // Gson no lanza error, pero crea un objeto con campos nulos/cero
        // Verificamos que al menos el código sea 0 por defecto y no rompa el sistema
        assertNotNull(agent);
        assertEquals(0, agent.code);
        assertNull(agent.username);
    }



}