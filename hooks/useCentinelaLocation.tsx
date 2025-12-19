import { useState, useEffect, useCallback } from 'react';
import * as Location from 'expo-location';

export const useCentinela = () => {

    const [location, setLocation] = useState<Location.LocationObject | null>(null);
    const [errorMsg, setErrorMsg] = useState<string | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    const getPosition = useCallback(async () => {

        setLoading(true);
        setErrorMsg(null);

        try {

            //Pedimos la autorizacion para acceder a la ubi
            const { status } = await Location.requestForegroundPermissionsAsync();

            if (status !== 'granted') {
                setErrorMsg('Denied permission to access location');
                setLoading(false);
                return;

            }

            //Rastreo de las coordenadas con Alta precision obligatoriamente

            const currentLocation = await Location.getCurrentPositionAsync({
                accuracy: Location.Accuracy.High,
            });

            setLocation(currentLocation);
            
        } catch (error) {
            setErrorMsg('Critic error satelite location has been interrupted');
        }finally{
            setLoading(false);
        }
    }, [])

    useEffect(() => {
        getPosition();
    }, [getPosition])

    return {
        location,
        errorMsg,
        loading,
        refresh: getPosition
    };

};