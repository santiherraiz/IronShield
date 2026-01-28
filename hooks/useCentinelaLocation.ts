import { useState, useEffect, useRef } from 'react';
import * as Location from 'expo-location';
import { usePermissionStore } from '../presentation/store/usePermissionStore';
import { PermissionStatus } from '../infrastructure/interfaces/location';

export const useCentinela = () => {
    const [location, setLocation] = useState<Location.LocationObject | null>(null);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);
    
    // Referencia para guardar la subscripción y poder cancelarla luego
    const subscription = useRef<Location.LocationSubscription | null>(null);

    const { locationStatus, checkLocationPermission } = usePermissionStore();

    useEffect(() => {
        const startWatching = async () => {
            setLoading(true);
            
            // 1. Verificación de permisos
            if (locationStatus !== PermissionStatus.GRANTED) {
                const status = await checkLocationPermission();
                if (status !== PermissionStatus.GRANTED) {
                    setErrorMsg('ACCESO SATELITAL RESTRINGIDO');
                    setLoading(false);
                    return;
                }
            }

            try {
                // 2. Iniciamos el rastreo en tiempo real
                subscription.current = await Location.watchPositionAsync(
                    {
                        accuracy: Location.Accuracy.High,
                        distanceInterval: 1, // Se actualiza cada 1 metro de movimiento
                        timeInterval: 2000,   // O cada 2 segundos
                    },
                    (newLocation) => {
                        // Esta función se ejecuta cada vez que el centinela se mueve
                        setLocation(newLocation);
                        setLoading(false);
                        setErrorMsg(null);
                    }
                );
            } catch (error) {
                setErrorMsg('ERROR CRÍTICO: SEÑAL INTERRUMPIDA');
                setLoading(false);
            }
        };

        startWatching();

        // 3. LIMPIEZA: Al salir de la pantalla, matamos el proceso de GPS
        return () => {
            if (subscription.current) {
                subscription.current.remove();
            }
        };
    }, [locationStatus]); // Se reinicia si cambian los permisos

    return {
        location,
        errorMsg,
        loading,
        status: locationStatus
    };
};