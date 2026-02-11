import { useEffect, useState } from 'react';
import * as Location from 'expo-location';
import { useServer } from './useServer';
import { useUser } from '../contexts/UserContext';
import { PermissionStatus } from '../infrastructure/interfaces/location';
import { checkLocationPermission, requestLocationPermission } from '../core/actions/permissions/location'; 

export const useCentinela = () => {
    const [location, setLocation] = useState(null);
    const [status, setStatus] = useState(PermissionStatus.CHECKING);
    const [loading, setLoading] = useState(true);
    
    const { sendLocation } = useServer();
    const { userId } = useUser();

    useEffect(() => {
        //Creamos una supscripcion para el rastreo continuo de ubicacion
        let subscription: Location.LocationSubscription;

        const initCentinela = async () => {
            setLoading(true);
            
            let currentStatus = await checkLocationPermission();

            // Si no está determinado o denegado, pedimos permiso 
            if (currentStatus !== PermissionStatus.GRANTED) {
                currentStatus = await requestLocationPermission();
            }

            setStatus(currentStatus);

            // Si al final tenemos permiso, activamos el rastreo 
            if (currentStatus === PermissionStatus.GRANTED) {
                subscription = await Location.watchPositionAsync(
                    {
                        accuracy: Location.Accuracy.High,
                        timeInterval: 5000,
                        distanceInterval: 10,
                    },
                    (newLocation) => {
                        setLocation(newLocation);
                        
                        // Envío automático al servidor TCP de IronShield
                        sendLocation(
                            userId,
                            newLocation.coords.latitude.toString(),
                            newLocation.coords.longitude.toString()
                        );
                    }
                );
            }
            
            setLoading(false);
        };

        initCentinela();

        return () => {
            if (subscription) subscription.remove();
        };
    }, [userId]);

    return { 
        location, 
        loading, 
        status, 
        errorMsg: status === PermissionStatus.DENIED ? 'ERROR DE PERMISOS' : null 
    }; 
};