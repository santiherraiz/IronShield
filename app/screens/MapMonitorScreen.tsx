import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Dimensions } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';
import { CentinelaDisplay } from '../../components/CentinelaDisplay';
import { useMapMonitorScreen } from '../../hooks/useMapMonitorScreen';

const { width } = Dimensions.get('window');

const PantallaMonitoreoMapa = () => {

    const { handleCerrar } = useMapMonitorScreen();

    return (
        <SafeAreaView style={styles.contenedor}>

            {/* Cabecera Flotante */}
            <View style={styles.cabeceraFlotante}>
                <View style={styles.etiquetaZona}>
                    <Text style={styles.textoZona}>ZONA: POLÍGONO NORTE</Text>
                </View>
                <View style={styles.indicadorSenal}>
                    <View style={styles.puntoSenal} />
                    <Text style={styles.textoSenal}>SEÑAL: FUERTE</Text>
                </View>
            </View>

            {/* Area Principal del Mapa (Más grande) */}
            <View style={styles.areaMapa}>
                <CentinelaDisplay />

                {/* Overlay de Coordenadas integradas en el mapa */}
                <View style={styles.overlayCoordenadas}>
                    <Text style={styles.labelCoords}>GPS ACTIVO</Text>
                </View>
            </View>

            {/* Panel Inferior Compacto */}
            <View style={styles.panelInferior}>
                <View style={styles.infoEstado}>
                    <Text style={styles.textoEstado}>SISTEMA OPERATIVO</Text>
                    <Text style={styles.subtextoEstado}>MONITOREO EN TIEMPO REAL</Text>
                </View>

                <TouchableOpacity style={styles.botonCerrar} onPress={handleCerrar}>
                    <Text style={styles.textoBotonCerrar}>FINALIZAR VIGILANCIA</Text>
                </TouchableOpacity>
            </View>

        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    contenedor: {
        flex: 1,
        backgroundColor: COLORS.background,
    },
    cabeceraFlotante: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingHorizontal: 20,
        paddingVertical: 15,
        backgroundColor: COLORS.background,
        borderBottomWidth: 1,
        borderBottomColor: COLORS.border,
        zIndex: 10,
    },
    etiquetaZona: {
        borderWidth: 1,
        borderColor: COLORS.primary,
        paddingHorizontal: 10,
        paddingVertical: 5,
        borderRadius: 4,
    },
    textoZona: {
        color: COLORS.primary,
        fontSize: 10,
        fontWeight: 'bold',
        letterSpacing: 1,
    },
    indicadorSenal: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: 6,
    },
    puntoSenal: {
        width: 8,
        height: 8,
        borderRadius: 4,
        backgroundColor: COLORS.success,
    },
    textoSenal: {
        color: COLORS.textHighlight,
        fontSize: 10,
        fontWeight: 'bold',
    },
    areaMapa: {
        flex: 1, // Ocupa todo el espacio disponible restante
        backgroundColor: '#000',
        position: 'relative',
        justifyContent: 'center',
        alignItems: 'center',
        overflow: 'hidden',
        borderBottomWidth: 1,
        borderBottomColor: COLORS.border,
    },
    overlayCoordenadas: {
        position: 'absolute',
        bottom: 20,
        left: 20,
        backgroundColor: 'rgba(0, 0, 0, 0.7)',
        padding: 8,
        borderLeftWidth: 2,
        borderLeftColor: COLORS.textHighlight,
    },
    labelCoords: {
        color: COLORS.text,
        fontSize: 8,
        letterSpacing: 2,
        marginBottom: 2,
    },
    valorCoords: {
        color: COLORS.textHighlight,
        fontSize: 12,
        fontFamily: 'monospace',
        fontWeight: 'bold',
    },
    panelInferior: {
        backgroundColor: COLORS.card,
        padding: 20,
        paddingBottom: 30, // Extra padding for bottom safe area visual
    },
    infoEstado: {
        marginBottom: 15,
        alignItems: 'center',
    },
    textoEstado: {
        color: COLORS.text,
        fontSize: 9,
        letterSpacing: 3,
        marginBottom: 2,
    },
    subtextoEstado: {
        color: COLORS.primary,
        fontSize: 11,
        fontWeight: 'bold',
    },
    botonCerrar: {
        backgroundColor: COLORS.primary,
        paddingVertical: 18,
        alignItems: 'center',
        borderRadius: 0, // Rectangular, industrial look
        shadowColor: COLORS.primary,
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.3,
        shadowRadius: 4,
        elevation: 5,
    },
    textoBotonCerrar: {
        color: '#000',
        fontWeight: '900',
        fontSize: 14,
        letterSpacing: 1,
    }
});

export default PantallaMonitoreoMapa;