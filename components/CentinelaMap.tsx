import React from 'react';
import { View, StyleSheet } from 'react-native';
import MapView, { Marker, PROVIDER_GOOGLE } from 'react-native-maps';

interface Props {
    latitude: number;
    longitude: number;
}

export const CentinelaMap = ({ latitude, longitude }: Props) => {
    return (
        <View style={styles.mapContainer}>
            <MapView
                provider={PROVIDER_GOOGLE}
                style={styles.map}
                region={{
                    latitude,
                    longitude,
                    latitudeDelta: 0.002, 
                    longitudeDelta: 0.002,
                }}
                showsUserLocation={true}
            >
                <Marker
                    coordinate={{ latitude, longitude }}
                    title="CENTINELA ACTIVO"
                    description="Posición en tiempo real"
                    pinColor="#22c55e" 
                />
            </MapView>
        </View>
    );
};

const styles = StyleSheet.create({
    mapContainer: {
        height: 250,
        width: '100%',
        borderWidth: 1,
        borderColor: '#166534',
        marginTop: 10,
        borderRadius: 4,
        overflow: 'hidden',
    },
    map: { width: '100%', height: '100%' },
});