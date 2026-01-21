import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';
import { CentinelaDisplay } from '../../components/CentinelaDisplay';
import { useMapMonitorScreen } from '../../hooks/useMapMonitorScreen';

const PantallaMonitoreoMapa = () => {

    const { handleCerrar } = useMapMonitorScreen();

    return (
        <SafeAreaView style={styles.contenedor}>
            <View style={styles.barraSuperior}>
                <Text style={styles.textoZona}>ZONA: POLÍGONO NORTE</Text>
                <View style={styles.indicadorSenal}>
                    <Text style={styles.textoSenal}>SEÑAL: FUERTE</Text>
                </View>
            </View>

            <View style={styles.marcadorMapa}>
                <View style={styles.radarOverlay}>
                    <CentinelaDisplay />
                    <View style={styles.coordFooter}>
                        <Text style={styles.labelCoords}>SISTEMA DE POSICIONAMIENTO GLOBAL</Text>
                        <Text style={styles.valorCoords}>ACTIVO // ALTA PRECISIÓN</Text>
                    </View>
                </View>
            </View>

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

                <TouchableOpacity style={styles.botonCerrar} onPress={handleCerrar}>
                    <Text style={styles.textoBotonCerrar}>CERRAR PANEL</Text>
                </TouchableOpacity>
            </View>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    contenedor: { 
        flex: 1, 
        backgroundColor: COLORS.background 
    },
    barraSuperior: { 
        flexDirection: 'row', 
        justifyContent: 'space-between', 
        padding: 15, 
        backgroundColor: COLORS.card, 
        borderBottomWidth: 1, 
        borderBottomColor: COLORS.border 
    },
    textoZona: { 
        color: COLORS.textHighlight, 
        fontSize: 10, 
        fontWeight: 'bold', 
        letterSpacing: 2 
    },
    indicadorSenal: { 
        backgroundColor: COLORS.success, 
        paddingHorizontal: 8, 
        paddingVertical: 2, 
        borderRadius: 4 
    },
    textoSenal: { 
        color: COLORS.primaryText, 
        fontSize: 9, 
        fontWeight: '900' 
    },
    marcadorMapa: { 
        flex: 2, 
        backgroundColor: COLORS.background, 
        justifyContent: 'center', 
        padding: 20 
    },
    radarOverlay: { 
        flex: 1, 
        justifyContent: 'center' 
    },
    coordFooter: { 
        marginTop: 20, 
        alignItems: 'center', 
        borderTopWidth: 1, 
        borderTopColor: COLORS.border, 
        paddingTop: 10 
    },
    labelCoords: { 
        color: COLORS.text, 
        fontSize: 8, 
        fontFamily: 'monospace' 
    },
    valorCoords: { 
        color: COLORS.primary,
        fontSize: 9, 
        fontFamily: 'monospace', 
        fontWeight: 'bold' 
    },
    panelCoordenadas: { 
        flex: 1, 
        padding: 25, 
        backgroundColor: COLORS.card,
        borderTopWidth: 2, 
        borderTopColor: COLORS.primary
    },
    tituloPanel: { 
        color: COLORS.text, 
        fontSize: 10, 
        letterSpacing: 3, 
        marginBottom: 20, 
        borderBottomWidth: 1, 
        borderBottomColor: COLORS.border, 
        paddingBottom: 5 
    },
    fila: { 
        flexDirection: 'row', 
        justifyContent: 'space-between', 
        marginBottom: 10 
    },
    etiqueta: { 
        color: COLORS.text, 
        fontSize: 11, 
        fontFamily: 'monospace' 
    },
    valor: { 
        color: COLORS.textHighlight, 
        fontSize: 11, 
        fontWeight: 'bold', 
        fontFamily: 'monospace' 
    },
    alertaContenedor: { 
        marginTop: 15, 
        padding: 10, 
        alignItems: 'center' 
    },
    textoAlerta: { 
        color: COLORS.success,
        fontSize: 10, 
        fontWeight: '900', 
        letterSpacing: 2 
    },
    botonCerrar: {
        backgroundColor: COLORS.primary,
        padding: 15,
        marginTop: 10,
        alignItems: 'center',
    },
    textoBotonCerrar: {
        color: 'black',
        fontWeight: 'bold',
    }
});

export default PantallaMonitoreoMapa;