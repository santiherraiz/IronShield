import React from 'react';
import { View, StyleSheet } from 'react-native';
import MapView, { Marker } from 'react-native-maps';

interface Props {
    latitude: number;
    longitude: number;
}

export const CentinelaMap = ({ latitude, longitude }: Props) => {
    return (
        <View style={styles.mapContainer}>
            <MapView
                //provider={PROVIDER_GOOGLE}
                style={styles.map}
                region={{ 
                    latitude,
                    longitude,
                    latitudeDelta: 0.002,
                    longitudeDelta: 0.005,
                }}
                customMapStyle={mapStyle} 
            >
                <Marker
                    coordinate={{ latitude, longitude }}
                    title="UNIDAD CENTINELA"
                    description="Localización actual detectada"
                    pinColor="#22c55e" 
                />
            </MapView>
        </View>
    );
};

const mapStyle = [
    { "elementType": "geometry", "stylers": [{ "color": "#000000" }] },
    { "elementType": "labels.text.fill", "stylers": [{ "color": "#166534" }] },
    { "featureLayer": "water", "elementType": "geometry", "stylers": [{ "color": "#000000" }] }
];

const styles = StyleSheet.create({
    mapContainer: {
        height: 200,
        width: '100%',
        borderWidth: 1,
        borderColor: '#14532d',
        marginTop: 10,
        borderRadius: 2, // Siguiendo tu diseño cuadrado
        overflow: 'hidden',
    },
    map: { width: '100%', height: '100%' },
});