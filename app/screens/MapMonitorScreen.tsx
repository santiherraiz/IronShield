import React from 'react';
import { View, Text, StyleSheet, Animated } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';
import { CentinelaDisplay } from '../../components/CentinelaDisplay';

const PantallaMonitoreoMapa = () => {
    const pulseAnim = React.useRef(new Animated.Value(1)).current;

    React.useEffect(() => {
        Animated.loop(
            Animated.sequence([
                Animated.timing(pulseAnim, { toValue: 0.4, duration: 1500, useNativeDriver: true }),
                Animated.timing(pulseAnim, { toValue: 1, duration: 1500, useNativeDriver: true }),
            ])
        ).start();
    }, []);

    return (
        <SafeAreaView style={styles.contenedor}>
            {/* Cabecera Táctica */}
            <View style={styles.barraSuperior}>
                <Text style={styles.textoZona}>ZONA: POLÍGONO NORTE</Text>
                <View style={styles.indicadorSenal}>
                    <Text style={styles.textoSenal}>SEÑAL: FUERTE</Text>
                </View>
            </View>

            {/* Visualizador de Mapa/Centinela */}
            <View style={styles.marcadorMapa}>
                <View style={styles.radarOverlay}>
                    <CentinelaDisplay />
                    <View style={styles.coordFooter}>
                        <Text style={styles.labelCoords}>SISTEMA DE POSICIONAMIENTO GLOBAL</Text>
                        <Text style={styles.valorCoords}>ACTIVO // ALTA PRECISIÓN</Text>
                    </View>
                </View>
            </View>

            {/* Panel de Telemetría */}
            <View style={styles.panelCoordenadas}>
                <Text style={styles.tituloPanel}>TELEMETRÍA EN VIVO</Text>
                
                <View style={styles.fila}>
                    <Text style={styles.etiqueta}>VELOCIDAD:</Text>
                    <Text style={styles.valor}>0.0 KM/H</Text>
                </View>
                
                <View style={styles.fila}>
                    <Text style={styles.etiqueta}>LATENCIA PING:</Text>
                    <Text style={styles.valor}>24 MS</Text>
                </View>
                
                <View style={styles.fila}>
                    <Text style={styles.etiqueta}>ESTADO DEL LINK:</Text>
                    <Text style={[styles.valor, { color: COLORS.primary }]}>ENCRIPTADO</Text>
                </View>

                <Animated.View style={[styles.alertaContenedor, { opacity: pulseAnim }]}>
                    <Text style={styles.textoAlerta}>SIN MOVIMIENTO DETECTADO</Text>
                </Animated.View>
            </View>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    contenedor: { flex: 1, backgroundColor: '#000' },
    barraSuperior: { 
        flexDirection: 'row', 
        justifyContent: 'space-between', 
        padding: 15, 
        backgroundColor: '#0a0a0a', 
        borderBottomWidth: 1, 
        borderBottomColor: '#166534' 
    },
    textoZona: { color: '#22c55e', fontSize: 10, fontWeight: 'bold', letterSpacing: 2 },
    indicadorSenal: { backgroundColor: '#22c55e', paddingHorizontal: 8, paddingVertical: 2, borderRadius: 1 },
    textoSenal: { color: 'black', fontSize: 9, fontWeight: '900' },
    marcadorMapa: { flex: 2, backgroundColor: '#00050a', justifyContent: 'center', padding: 20 },
    radarOverlay: { flex: 1, justifyContent: 'center' },
    coordFooter: { marginTop: 20, alignItems: 'center', borderTopWidth: 1, borderTopColor: 'rgba(22, 101, 52, 0.2)', paddingTop: 10 },
    labelCoords: { color: '#166534', fontSize: 8, fontFamily: 'monospace' },
    valorCoords: { color: '#22c55e', fontSize: 9, fontFamily: 'monospace', fontWeight: 'bold' },
    panelCoordenadas: { 
        flex: 1, 
        padding: 25, 
        backgroundColor: '#000', 
        borderTopWidth: 2, 
        borderTopColor: '#22c55e' 
    },
    tituloPanel: { color: '#e2e8f0', fontSize: 10, letterSpacing: 3, marginBottom: 20, borderBottomWidth: 1, borderBottomColor: 'rgba(34, 197, 94, 0.2)', paddingBottom: 5 },
    fila: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 10 },
    etiqueta: { color: '#94a3b8', fontSize: 11, fontFamily: 'monospace' },
    valor: { color: '#4ade80', fontSize: 11, fontWeight: 'bold', fontFamily: 'monospace' },
    alertaContenedor: { marginTop: 15, padding: 10, alignItems: 'center' },
    textoAlerta: { color: '#22c55e', fontSize: 10, fontWeight: '900', letterSpacing: 2 }
});

export default PantallaMonitoreoMapa;