import { useState, useEffect, useCallback } from 'react';
import * as Location from 'expo-location';

export const useCentinela = () => {
    const [location, setLocation] = useState(null);
    const [errorMsg, setErrorMsg] = useState(null);
    const [loading, setLoading] = useState(true);

    const getPosition = useCallback(async () => {
        setLoading(true);
        setErrorMsg(null);
        try {
            const { status } = await Location.requestForegroundPermissionsAsync();
            if (status !== 'granted') {
                setErrorMsg('PERMISO DENEGADO: ACCESO SATELITAL RESTRINGIDO');
                setLoading(false);
                return;
            }
            const currentLocation = await Location.getCurrentPositionAsync({
                accuracy: Location.Accuracy.High,
            });
            setLocation(currentLocation);
        } catch (error) {
            setErrorMsg('ERROR CRÍTICO: SEÑAL INTERRUMPIDA');
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        getPosition();
    }, [getPosition]);

    return { location, errorMsg, loading, refresh: getPosition };
};