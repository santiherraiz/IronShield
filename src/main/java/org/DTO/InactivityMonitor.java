package org.DTO;

import org.Config.AppConfig;
import org.DAO.GuardiaDAO;
import org.DAO.InactividadDAO;
import org.Server.LogManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Monitor de inactividad que verifica periódicamente el estado de los guardias.
 * Detecta guardias que no han enviado señal en el tiempo configurado y registra alertas.
 */
public class InactivityMonitor {
    private static Timer timer;
    private static boolean isRunning = false;
    private static Set<String> currentInactiveGuards = new HashSet<>();
    
    /**
     * Inicia el monitor de inactividad.
     * El monitor verificará guardias inactivos cada POLLING_INTERVAL_MS milisegundos.
     */
    public static void start() {
        if (!isRunning) {
            LogManager.warn("El monitor de inactividad ya está en ejecución");
            return;
        }
        
        timer = new Timer("Hilo_Inactividad", true);
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkInactiveGuards();
            }
        }, 0, AppConfig.POLLING_INTERVAL_MS);
        
        isRunning = true;
    }
    
    /**
     * Detiene el monitor de inactividad.
     */
    public static void stop() {
        if (!isRunning) {
            LogManager.warn("El monitor de inactividad no está en ejecución");
            return;
        }
        
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        
        isRunning = false;
        currentInactiveGuards.clear();
    }
    
    /**
     * Verifica el estado de los guardias y detecta inactividad.
     * Esta función:
     * 1. Obtiene todos los guardias registrados
     * 2. Obtiene los guardias activos
     * 3. Compara ambas listas para detectar inactivos
     * 4. Registra alertas de inactividad cuando es necesario
     */
    private static void checkInactiveGuards() {
        try {
            // Obtener todos los guardias que alguna vez enviaron posición
            List<Agent> todosLosGuardias = GuardiaDAO.obtenerTodosLosGuardias();
            
            // Obtener solo los guardias activos (últimos N segundos)
            List<Agent> guardiaActivos = GuardiaDAO.obtenerTodos();
            
            // Crear un Set con los usernames activos para búsqueda rápida
            final Set<String> usernames_activos = new HashSet<>();
            for (final Agent activo : guardiaActivos) {
                usernames_activos.add(activo.username);
            }
            
            // Detectar guardias inactivos
            final Set<String> nuevosInactivos = new HashSet<>();
            
            for (Agent guardia : todosLosGuardias) {
                boolean estaActivo = usernames_activos.contains(guardia.username);
                
                if (!estaActivo) {
                    nuevosInactivos.add(guardia.username);
                    
                    // Solo registrar si es un nuevo inactivo (no estaba en la lista anterior)
                    if (!currentInactiveGuards.contains(guardia.username)) {
                        // Registrar inactividad en la base de datos
                        InactividadDAO.insertarInactividad(guardia.username);
                    }
                }
            }
            
            // Actualizar la lista de inactivos actual
            currentInactiveGuards = nuevosInactivos;
            
        } catch (Exception e) {
            LogManager.error("Error en el monitor de inactividad: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene el estado actual del monitor. 
     */
    public static boolean isRunning() { return isRunning; }
}