import { useState, useEffect, useCallback } from 'react';
import * as Location from 'expo-location';
import { usePermissionStore } from '../presentation/store/usePermissionStore';
import { PermissionStatus } from '../infrastructure/interfaces/location';

export const useCentinela = () => {
    const [location, setLocation] = useState<Location.LocationObject | null>(null);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    // Obtenemos el estado y las funciones del Store profesional
    const { locationStatus, checkLocationPermission, requestLocationPermission } = usePermissionStore();

    const getPosition = useCallback(async () => {
        // 1. Si no hemos comprobado permisos, lo hacemos
        if (locationStatus === PermissionStatus.CHECKING) {
            await checkLocationPermission();
            return;
        }

        // 2. Si están denegados, solicitamos (esto disparará el Alert manual si es necesario)
        if (locationStatus !== PermissionStatus.GRANTED) {
            const status = await requestLocationPermission();
            if (status !== PermissionStatus.GRANTED) {
                setErrorMsg('ACCESO SATELITAL RESTRINGIDO: REVISE AJUSTES');
                return;
            }
        }

        // 3. Si están concedidos, obtenemos la posición
        setLoading(true);
        setErrorMsg(null);
        try {
            const currentLocation = await Location.getCurrentPositionAsync({
                accuracy: Location.Accuracy.High,
            });
            setLocation(currentLocation);
        } catch (error) {
            setErrorMsg('ERROR CRÍTICO: SEÑAL INTERRUMPIDA');
        } finally {
            setLoading(false);
        }
    }, [locationStatus]);

    // Sincronización inicial
    useEffect(() => {
        getPosition();
    }, [locationStatus]); // Se dispara cuando cambie el estado global de permisos

    return { 
        location, 
        errorMsg, 
        loading, 
        refresh: getPosition,
        status: locationStatus // Útil para que el componente sepa el estado exacto
    };
};