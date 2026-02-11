import { useEffect, useState } from 'react';
import * as Location from 'expo-location';
import { useServer } from './useServer';
import { useUser } from '../contexts/UserContext';
import { PermissionStatus } from '../infrastructure/interfaces/location';

export const useCentinela = () => {
    const [location, setLocation] = useState(null);
    const [status, setStatus] = useState(PermissionStatus.CHECKING); // Estado inicial
    const [loading, setLoading] = useState(true);
    
    const { sendLocation } = useServer();
    const { userId } = useUser();

    useEffect(() => {
        let subscription;

        const startTracking = async () => {
            try {
                const { status: authStatus } = await Location.requestForegroundPermissionsAsync();
                
                if (authStatus !== 'granted') {
                    setStatus(PermissionStatus.DENIED);
                    setLoading(false);
                    return;
                }

                setStatus(PermissionStatus.GRANTED);
                setLoading(false);

                subscription = await Location.watchPositionAsync(
                    {
                        accuracy: Location.Accuracy.High,
                        timeInterval: 5000,
                        distanceInterval: 10,
                    },
                    (newLocation) => {
                        setLocation(newLocation);
                        // Enviar al servidor TCP de IronShield
                        sendLocation(
                            userId,
                            newLocation.coords.latitude.toString(),
                            newLocation.coords.longitude.toString()
                        );
                    }
                );
            } catch (error) {
                console.error(error);
                setLoading(false);
            }
        };

        startTracking();

        return () => {
            if (subscription) subscription.remove();
        };
    }, [userId]);

    return { location, loading, status, errorMsg: null }; 
};