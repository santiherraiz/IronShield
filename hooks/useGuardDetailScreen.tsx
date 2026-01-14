import { useNavigation } from 'expo-router';
import { sendErrorToJava } from '../app/services/LogService';

export const useGuardDetailScreen = () => {

    const navigation = useNavigation<any>();

    const handleCerrar = () => {
        try {
            if (navigation.canGoBack()) {
                navigation.goBack();
            } else {
                navigation.navigate('Dashboard');
            }
        } catch (error) {
            const msg = error instanceof Error ? error.message : String(error);
            sendErrorToJava(msg, 'GuardDetailScreen');
        }
    };

    return {
        handleCerrar
    };
};