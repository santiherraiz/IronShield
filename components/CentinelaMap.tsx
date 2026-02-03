import React from 'react';
import { View, StyleSheet } from 'react-native';
import MapView, { Marker, PROVIDER_GOOGLE } from 'react-native-maps';
import { ShieldAlert } from 'lucide-react-native';

interface Props {
    latitude: number;
    longitude: number;
}

export const CentinelaMap = ({ latitude, longitude }: Props) => {

    const balizas = [
        {id: 1, title: 'Baliza 1', lat: 39.4589, lng: -0.4696},
        {id: 2, title: 'Baliza 2', lat: 39.4587, lng: -0.4684},
        {id: 3, title: 'Baliza 3', lat: 39.4594, lng: -0.4688},
        {id: 4, title: 'Baliza 4', lat: 39.4593, lng: -0.4699}
    ]

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

                {balizas.map((baliza) => (
                    <Marker
                        key={baliza.id}
                        coordinate={{latitude: baliza.lat , longitude: baliza.lng}}
                        description='Punto de control obligatorio'
                    
                    >
                        <View style={styles.balizaMarker}>
                            <ShieldAlert size={14} color="white"/>
                        </View>
                    </Marker>
                ))}

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
    balizaMarker: {
        backgroundColor: '#f97316',
        padding: 9,
        borderRadius: 20,
        borderWidth: 2,
        borderColor: 'white',
        elevation: 5,
    }
});