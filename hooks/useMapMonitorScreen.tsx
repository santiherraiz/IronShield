import React from 'react';
import { Animated } from 'react-native';
import { sendErrorToJava } from '../app/services/LogService';

export const useMapMonitorScreen = () => {
    
    const pulseAnim = React.useRef(new Animated.Value(1)).current;

    React.useEffect(() => {
        try {
            Animated.loop(
                Animated.sequence([
                    Animated.timing(pulseAnim, { toValue: 0.4, duration: 1500, useNativeDriver: true }),
                    Animated.timing(pulseAnim, { toValue: 1, duration: 1500, useNativeDriver: true }),
                ])
            ).start();
        } catch (error) {
            const msg = error instanceof Error ? error.message : String(error);
            sendErrorToJava(msg, 'MapMonitorScreen');
        }
    }, []);
    
    return {
        pulseAnim
    };
}