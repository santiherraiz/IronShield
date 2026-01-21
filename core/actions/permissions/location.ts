import * as Location from 'expo-location'
import { PermissionStatus } from '../../../infrastructure/interfaces/location'
import { Alert, Linking } from 'react-native';
export const requestLocationPermission = async (): Promise<PermissionStatus> => {


    const { status } = await Location.requestForegroundPermissionsAsync();


    if (status !== 'granted') {
        //TODO Crear la peticion manual de cambio de permisos
        if (status === 'denied') {
            manualPermissionRequest();
        }
        return PermissionStatus.DENIED;
    }
    //Faltan los estados intermedios
    return PermissionStatus.GRANTED;
}

export const checkLocationPermission = async () => {
    // Comprobamos si tenemos permisos para poder acceder más tarde a los recursos del sistema

    const { status } = await Location.getForegroundPermissionsAsync();


    switch (status) {
        case 'granted':
            return PermissionStatus.GRANTED;
        case 'denied':
            return PermissionStatus.DENIED;
        default:
            return PermissionStatus.UNDETERMINATED;
    }

}
const manualPermissionRequest = async () => {
    // Lanzamos un accesso a los ajustes de la aplicación para cambiar manualmente los permisos

    Alert.alert('Permiso de ubicacion necesario', 'Para continuar debe habilitar el permiso de localización en los ajustes de la app',

        [
            {
                text: 'Abrir Ajustes',
                onPress: () => {
                    Linking.openSettings();
                }
            },
            {
                text: 'Cancelar',
                style: 'destructive'
            }
        ]

    )
}