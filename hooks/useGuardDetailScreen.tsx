import { useNavigation } from 'expo-router';
import { sendErrorToJava } from '../app/services/LogService';

export const useGuardDetailScreen = () => {

    const navigation = useNavigation<any>();

    const handleCerrar = () => {
        try {
            navigation.replace('Login');
        } catch (error) {
            const msg = error instanceof Error ? error.message : String(error);
            sendErrorToJava(msg, 'GuardDetailScreen');
        }
    };

    return {
        handleCerrar
    };
};