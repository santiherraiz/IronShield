import React from 'react';
import { View, Text, ActivityIndicator, TouchableOpacity, StyleSheet } from 'react-native';
import { useCentinela } from '../hooks/useCentinelaLocation';
import { Radar, RefreshCw, AlertOctagon, ShieldCheck } from 'lucide-react-native';

export const CentinelaDisplay = () => {
    const { location, errorMsg, loading, refresh } = useCentinela();

    return (
        <View style={styles.card}>
            {/* Header táctico */}
            <View style={styles.cardHeader}>
                <View style={styles.row}>
                    <Radar size={16} color="#22c55e" />
                    <Text style={styles.headerTitle}>CENTINELA V1.0 // EN LÍNEA</Text>
                </View>
                <TouchableOpacity onPress={refresh} disabled={loading}>
                    <RefreshCw size={16} color={loading ? "#14532d" : "#22c55e"} />
                </TouchableOpacity>
            </View>

            {loading ? (
                <View style={styles.centerPadding}>
                    <ActivityIndicator color="#22c55e" size="small" />
                    <Text style={styles.loadingText}>SINCRONIZANDO CON SATÉLITE...</Text>
                </View>
            ) : errorMsg ? (
                <View style={styles.errorBox}>
                    <AlertOctagon size={20} color="#dc2626" />
                    <Text style={styles.errorText}>{errorMsg}</Text>
                </View>
            ) : (
                <View>
                    {/* Panel de Coordenadas */}
                    <View style={styles.dataContainer}>
                        <View style={styles.dataRow}>
                            <Text style={styles.dataLabel}>LATITUD:</Text>
                            <Text style={styles.dataValue}>{location?.coords.latitude.toFixed(6)}</Text>
                        </View>
                        <View style={styles.dataRow}>
                            <Text style={styles.dataLabel}>LONGITUD:</Text>
                            <Text style={styles.dataValue}>{location?.coords.longitude.toFixed(6)}</Text>
                        </View>
                    </View>

                    {/* Footer de Integridad */}
                    <View style={styles.cardFooter}>
                        <View style={styles.row}>
                            <ShieldCheck size={12} color="#166534" />
                            <Text style={styles.precisionText}>
                                PRECISIÓN: {location?.coords.accuracy?.toFixed(1)}m
                            </Text>
                        </View>
                        <View style={styles.verifiedBadge}>
                            <Text style={styles.verifiedText}>VERIFICADO</Text>
                        </View>
                    </View>
                </View>
            )}
        </View>
    );
};

const styles = StyleSheet.create({
    card: { backgroundColor: '#000', padding: 15, borderWidth: 1, borderColor: '#166534', borderRadius: 2 },
    cardHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', borderBottomWidth: 1, borderBottomColor: '#14532d', paddingBottom: 10, marginBottom: 15 },
    row: { flexDirection: 'row', alignItems: 'center' },
    headerTitle: { color: '#22c55e', fontSize: 10, fontFamily: 'monospace', marginLeft: 8, fontWeight: 'bold', letterSpacing: 1 },
    centerPadding: { paddingVertical: 20, alignItems: 'center' },
    loadingText: { color: '#22c55e', fontSize: 8, fontFamily: 'monospace', marginTop: 10, letterSpacing: 1 },
    errorBox: { flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(220, 38, 38, 0.1)', padding: 10, borderLeftWidth: 3, borderLeftColor: '#dc2626' },
    errorText: { color: '#ef4444', fontSize: 10, fontFamily: 'monospace', marginLeft: 10, fontWeight: 'bold' },
    dataContainer: { backgroundColor: 'rgba(34, 197, 94, 0.05)', padding: 12, borderLeftWidth: 1, borderLeftColor: '#22c55e' },
    dataRow: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 5 },
    dataLabel: { color: '#166534', fontSize: 9, fontFamily: 'monospace', fontWeight: 'bold' },
    dataValue: { color: '#4ade80', fontSize: 14, fontFamily: 'monospace', fontWeight: 'bold' },
    cardFooter: { marginTop: 15, paddingTop: 10, borderTopWidth: 1, borderTopColor: 'rgba(22, 101, 52, 0.2)', flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
    precisionText: { color: '#166534', fontSize: 8, fontFamily: 'monospace', marginLeft: 5 },
    verifiedBadge: { backgroundColor: 'rgba(22, 101, 52, 0.3)', paddingHorizontal: 5, paddingVertical: 2 },
    verifiedText: { color: '#22c55e', fontSize: 8, fontWeight: 'bold' }
});