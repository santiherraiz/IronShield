import { useEffect, useState } from 'react';
import * as Location from 'expo-location';
import { useServer } from './useServer';
import { useUser } from '../contexts/UserContext';

export const useCentinela = () => {

    const [location, setLocation] = useState(null);
    const { sendLocation } = useServer();
    const { userId } = useUser();
    useEffect(() => {
        let subscription;
        const startTracking = async () => {
            const { status } = await Location.requestForegroundPermissionsAsync();
            if (status === 'granted') {
                subscription = await Location.watchPositionAsync(
                    {
                        accuracy: Location.Accuracy.High,
                        timeInterval: 5000,
                        distanceInterval: 10,
                    },
                    (newLocation) => {
                        setLocation(newLocation);

                        sendLocation(
                            userId,
                            newLocation.coords.latitude.toString(),
                            newLocation.coords.longitude.toString()
                        );
                    }
                );
            }
        };
        startTracking();
        return () => {
            if (subscription) {
                subscription.remove();
            }
        };
    }, [userId]);
    return { location };
};