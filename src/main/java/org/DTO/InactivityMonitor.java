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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Monitor de inactividad que verifica periódicamente el estado de los guardias.
 * Detecta guardias que no han enviado señal en el tiempo configurado y registra alertas.
 */
public class InactivityMonitor {
    
    private static Timer timer;
    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static Set<String> currentInactiveGuards = new HashSet<>();
    
    /**
     * Inicia el monitor de inactividad.
     * El monitor verificará guardias inactivos cada POLLING_INTERVAL_MS milisegundos.
     */
    public static void start() {
        if (isRunning.get()) {
            LogManager.warn("El monitor de inactividad ya está en ejecución");
            return;
        }
        
        timer = new Timer("InactivityMonitor-Thread", true);
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkInactiveGuards();
            }
        }, 0, AppConfig.POLLING_INTERVAL_MS);
        
        isRunning.set(true);
        
        LogManager.info(String.format(
            "✓ Monitor de inactividad iniciado correctamente%n" +
            "  - Umbral de inactividad: %d segundos%n" +
            "  - Intervalo de verificación: %d segundos",
            AppConfig.INACTIVITY_THRESHOLD_SECONDS,
            AppConfig.getPollingIntervalSeconds()
        ));
    }
    
    /**
     * Detiene el monitor de inactividad.
     */
    public static void stop() {
        if (!isRunning.get()) {
            LogManager.warn("El monitor de inactividad no está en ejecución");
            return;
        }
        
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        
        isRunning.set(false);
        currentInactiveGuards.clear();
        
        LogManager.info("Monitor de inactividad detenido");
    }
    
    /**
     * Verifica el estado de los guardias y detecta inactividad.
     * Este método:
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
            Set<String> usernames_activos = new HashSet<>();
            for (Agent activo : guardiaActivos) {
                usernames_activos.add(activo.username);
            }
            
            // Detectar guardias inactivos
            Set<String> nuevosInactivos = new HashSet<>();
            int contadorInactivos = 0;
            
            for (Agent guardia : todosLosGuardias) {
                boolean estaActivo = usernames_activos.contains(guardia.username);
                
                if (!estaActivo) {
                    nuevosInactivos.add(guardia.username);
                    contadorInactivos++;
                    
                    // Solo registrar si es un nuevo inactivo (no estaba en la lista anterior)
                    if (!currentInactiveGuards.contains(guardia.username)) {
                        // Registrar inactividad en la base de datos
                        boolean registrado = InactividadDAO.insertarInactividad(guardia.username);
                        
                        if (registrado) {
                            // Calcular tiempo inactivo
                            long tiempoInactivo = System.currentTimeMillis() - guardia.date;
                            int segundosInactivo = (int) (tiempoInactivo / 1000);
                            
                            LogManager.warn(String.format(
                                "ALERTA DE INACTIVIDAD:%n" +
                                "  - Guardia: %s%n" +
                                "  - Última señal: hace %d segundos%n" +
                                "  - Última posición: (%.6f, %.6f)%n" +
                                "  - Total inactividades hoy: %d",
                                guardia.username,
                                segundosInactivo,
                                guardia.latitude,
                                guardia.longitude,
                                InactividadDAO.contarInactividadesHoy(guardia.username)
                            ));
                        }
                    }
                }
            }
            
            // Detectar guardias que se reactivaron (estaban inactivos y ahora están activos)
            Set<String> reactivados = new HashSet<>(currentInactiveGuards);
            reactivados.removeAll(nuevosInactivos);
            
            for (String reactivado : reactivados) {
                LogManager.info(String.format(
                    "Guardia %s se ha REACTIVADO", reactivado
                ));
            }
            
            // Actualizar la lista de inactivos actual
            currentInactiveGuards = nuevosInactivos;
            
            // Log de resumen cada cierto tiempo (cada 10 verificaciones)
            if (System.currentTimeMillis() % (AppConfig.POLLING_INTERVAL_MS * 10) < AppConfig.POLLING_INTERVAL_MS) {
                LogManager.info(String.format(
                    "Resumen de monitoreo:%n" +
                    "  - Total guardias: %d%n" +
                    "  - Guardias activos: %d%n" +
                    "  - Guardias inactivos: %d",
                    todosLosGuardias.size(),
                    guardiaActivos.size(),
                    contadorInactivos
                ));
            }
            
        } catch (Exception e) {
            LogManager.error("Error en el monitor de inactividad: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Obtiene el estado actual del monitor. 
     */
    public static boolean isRunning() {
        return isRunning.get();
    }
    
    /**
     * Obtiene el conjunto de guardias actualmente inactivos.
     */
    public static Set<String> getCurrentInactiveGuards() {
        return new HashSet<>(currentInactiveGuards);
    }
    
    /**
     * Obtiene estadísticas del monitor.
    */
    public static String getStats() {
        return String.format(
            "Monitor de Inactividad:%n" +
            "  - Estado: %s%n" +
            "  - Guardias inactivos actuales: %d%n" +
            "  - Umbral: %d segundos%n" +
            "  - Intervalo de polling: %d ms",
            isRunning.get() ? "ACTIVO" : "INACTIVO",
            currentInactiveGuards.size(),
            AppConfig.INACTIVITY_THRESHOLD_SECONDS,
            AppConfig.POLLING_INTERVAL_MS
        );
    }
    
    /**
     * Fuerza una verificación inmediata de guardias inactivos.
     */
    public static void forceCheck() {
        if (!isRunning.get()) {
            LogManager.warn("No se puede forzar verificación: el monitor no está activo");
            return;
        }
        
        LogManager.info("Forzando verificación de inactividad...");
        checkInactiveGuards();
    }
}