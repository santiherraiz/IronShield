import React, { useState } from 'react';
import { View, Text, StyleSheet, FlatList, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';
import { useGuardDetailScreen } from '../../hooks/useGuardDetailScreen';
import { useUser } from '../../contexts/UserContext';
import { useRoute } from "@react-navigation/core";
import { useServer } from '../../hooks/useServer';

type Agent = {
    username: string;
    latitude: number;
    longitude: number;
    date: number | string;
};

const PantallaDetalleGuardia = () => {

    const { handleCerrar } = useGuardDetailScreen();
    const { userId } = useUser();
    const route = useRoute();
    const { name } = route.params as { name: string };
    const { getActiveGuards } = useServer();

    // Guardias y su estado de carga
    const [guards, setGuards] = useState<Agent[]>([]);
    const [loading, setLoading] = useState(false);

    // Polling de guardias activos
    React.useEffect(() => {
        const fetchGuards = async () => {
            const data = await getActiveGuards();
            if (Array.isArray(data)) {
                setGuards(data);
            }
        };

        fetchGuards(); // Initial fetch
        const interval = setInterval(fetchGuards, 5000); // Poll every 5s

        return () => clearInterval(interval);
    }, []);



    // Renderiza cada guardia con su estado (Activo/Inactivo)

    const renderGuardia = ({ item }: { item: Agent }) => {
        const lastActive = new Date(item.date);
        const now = new Date();
        const diffSeconds = (now.getTime() - lastActive.getTime()) / 1000;
        const isActive = diffSeconds < 30;

        const hora = lastActive.toLocaleTimeString([], {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });

        return (
            <View style={styles.itemRegistro}>
                <View style={[styles.indicadorRegistro, { backgroundColor: isActive ? COLORS.success : COLORS.danger }]} />
                <View style={{ flex: 1 }}>
                    <Text style={styles.tipoRegistro}>
                        {item.username.toUpperCase()} ({item.latitude.toFixed(3)}, {item.longitude.toFixed(3)})
                    </Text>
                </View>
                <Text style={styles.horaRegistro}>{hora}</Text>
            </View>
        );
    };

    return (
        <SafeAreaView style={styles.contenedor}>
            <View style={styles.encabezado}>
                <Text style={styles.tituloPantalla}>PANEL DE SUPERVISIÓN (EN VIVO)</Text>
            </View>

            <View style={styles.cuadriculaInfo}>
                <View style={styles.cajaInfo}>
                    <Text style={styles.etiqueta}>SUPERVISOR</Text>
                    <Text style={styles.valor}>{userId}</Text>
                </View>
                <View style={styles.cajaInfo}>
                    <Text style={styles.etiqueta}>NOMBRE</Text>
                    <Text style={styles.valor}>{name.toUpperCase()}</Text>
                </View>
            </View>

            <View style={styles.seccionRegistros}>
                <Text style={styles.encabezadoSeccion}>ESTADO DE GUARDIAS</Text>

                <FlatList
                    data={guards}
                    keyExtractor={(item, index) => `${item.username}-${index}`}
                    renderItem={renderGuardia}
                    ListEmptyComponent={
                        <Text style={{ color: COLORS.text, textAlign: 'center' }}>
                            Cargando o no hay guardias activos...
                        </Text>
                    }
                />
            </View>

            <TouchableOpacity style={styles.botonCerrar} onPress={handleCerrar}>
                <Text style={styles.textoBotonCerrar}>CERRAR PANEL</Text>
            </TouchableOpacity>
        </SafeAreaView>
    );
};

const styles = StyleSheet.create({
    contenedor: {
        flex: 1,
        backgroundColor: COLORS.background,
        padding: 10,
    },
    encabezado: {
        paddingVertical: 15,
        borderBottomWidth: 1,
        borderBottomColor: COLORS.border,
        marginBottom: 10,
    },
    tituloPantalla: {
        color: COLORS.primary,
        fontSize: 16,
        fontWeight: 'bold',
        letterSpacing: 1,
    },
    cuadriculaInfo: {
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
        marginBottom: 20,
    },
    cajaInfo: {
        width: '48%',
        backgroundColor: 'transparent',
        borderWidth: 1,
        borderColor: COLORS.text,
        padding: 10,
        marginBottom: 10,
    },
    etiqueta: {
        color: COLORS.primary,
        fontSize: 10,
        marginBottom: 5,
    },
    valor: {
        color: COLORS.textHighlight,
        fontSize: 14,
        fontWeight: 'bold',
    },
    seccionRegistros: {
        flex: 1,
        borderWidth: 1,
        borderColor: COLORS.border,
        padding: 10,
    },
    encabezadoSeccion: {
        color: COLORS.primary,
        marginBottom: 10,
        fontSize: 12,
    },
    itemRegistro: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#000',
        marginBottom: 5,
        padding: 10,
        borderLeftWidth: 3,
        borderLeftColor: COLORS.success,
    },
    indicadorRegistro: {
        width: 8,
        height: 8,
        backgroundColor: COLORS.success,
        marginRight: 10,
    },
    tipoRegistro: {
        color: COLORS.text,
        fontSize: 12,
    },
    horaRegistro: {
        color: COLORS.textHighlight,
        fontSize: 12,
        fontFamily: 'monospace',
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

export default PantallaDetalleGuardia;