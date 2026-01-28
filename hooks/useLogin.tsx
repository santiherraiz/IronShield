
import { useState } from 'react';
import { Alert } from 'react-native';
import { useNavigation } from 'expo-router';
import { useUser } from '../contexts/UserContext';
import {useServer} from "./useServer";

export const useLogin = () => {
    const navigation = useNavigation<any>();
    const { setRole, setUserId, setUserName } = useUser();
    const [idServicio, setIdServicio] = useState('');
    const [contrasena, setContrasena] = useState('');

    const gestionarInicioSesion = async () => {
        try {
            if (!idServicio.trim() || !contrasena.trim()) {
                throw new Error('ID de servicio y contraseña son requeridos');
            }

            const guardiaRegex = /^G\d{3}$/;
            const supervisorRegex = /^S\d{3}$/;

            const name = await useServer().getName(idServicio, contrasena);

            setUserName(name);

            let role: 'guardia' | 'supervisor';
            if (guardiaRegex.test(idServicio)) {
                role = 'guardia';
                setRole('guardia');
                setUserId(idServicio);
                navigation.replace('OperationsStack');
            } else if (supervisorRegex.test(idServicio)) {
                role = 'supervisor';
                setRole('supervisor');
                setUserId(idServicio);
                navigation.replace('GuardDetail');
            } else {
                throw new Error('ID de servicio inválido.');
            }

        } catch (error) {
            const msg = error instanceof Error ? error.message : String(error);
            Alert.alert('Error', msg);
        }
    };

    return {
        idServicio,
        setIdServicio,
        contrasena,
        setContrasena,
        gestionarInicioSesion,

    };
};