import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';
import { useDashboardScreen } from '../../hooks/useDashboardScreen';
import { useUser } from '../../contexts/UserContext';
import { useCentinela } from "../../hooks/useCentinelaLocation";
import { useServer } from "../../hooks/useServer";
import { useLogin } from "../../hooks/useLogin";
import { useRoute } from "@react-navigation/core";

const PantallaPanelControl = ({ navigation }: any) => {

  const { estado, setEstado, gestionarPuntoControl } = useDashboardScreen();
  const { role, userId } = useUser();
  const { dia, mes, anno } = {
    dia: new Date().getDate(),
    mes: new Date().getMonth() + 1,
    anno: new Date().getFullYear()
  }
  const { location } = useCentinela();
  const { sendLocation } = useServer();
  const { contrasena } = useLogin();
  const route = useRoute();
  const { name } = route.params;

  return (
    <SafeAreaView style={styles.contenedor}>
      <View style={styles.filaEncabezado}>
        <View>
          <Text style={styles.etiquetaEncabezado}>GUARDIA</Text>
          <Text style={styles.valorEncabezado}>{userId} - {(name as string).toUpperCase()}</Text>
        </View>
        <View style={{ alignItems: 'flex-end' }}>
          <Text style={styles.etiquetaEncabezado}>FECHA</Text>
          <Text style={styles.valorEncabezado}> {dia}/{mes}/{anno}</Text>
        </View>
      </View>

      <View style={styles.contenedorAccionPrincipal}>
        <Text style={styles.textoInstruccion}>
          AL LLEGAR A POSICIÓN, PULSE PARA CONFIRMAR PRESENCIA
        </Text>

        <TouchableOpacity style={styles.botonPuntoControl} onPress={() => sendLocation(
          userId,
          location?.coords.latitude.toFixed(6),
          location?.coords.longitude.toFixed(6))}
        >
          <View style={styles.anilloInterno}>
            <Text style={styles.textoPuntoControl}>PUNTO DE CONTROL</Text>
            <Text style={styles.subtextoPuntoControl}>ASEGURADO</Text>
          </View>
        </TouchableOpacity>
      </View>

      <View style={styles.navegacionCuadricula}>
        <TouchableOpacity
          style={styles.tarjetaNav}
          onPress={() => navigation.navigate('MapMonitor')}>
          <Text style={styles.tituloNav}>MAPA / GPS</Text>
          <Text style={styles.estadoNav}>RASTREO ACTIVO</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  contenedor: {
    flex: 1,
    backgroundColor: COLORS.background,
    padding: 15,
  },
  filaEncabezado: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 30,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
    paddingBottom: 15,
  },
  etiquetaEncabezado: {
    color: COLORS.text,
    fontSize: 10,
    letterSpacing: 1,
  },
  valorEncabezado: {
    color: COLORS.textHighlight,
    fontSize: 16,
    fontWeight: 'bold',
    marginTop: 2,
  },
  contenedorAccionPrincipal: {
    flex: 2,
    justifyContent: 'center',
    alignItems: 'center',
  },
  textoInstruccion: {
    color: COLORS.text,
    textAlign: 'center',
    marginBottom: 20,
    maxWidth: '80%',
  },
  botonPuntoControl: {
    width: 250,
    height: 250,
    borderRadius: 125,
    backgroundColor: COLORS.primary,
    justifyContent: 'center',
    alignItems: 'center',
    elevation: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 5 },
    shadowOpacity: 0.5,
    shadowRadius: 5,
  },
  anilloInterno: {
    width: 230,
    height: 230,
    borderRadius: 115,
    borderWidth: 2,
    borderColor: COLORS.primaryText,
    justifyContent: 'center',
    alignItems: 'center',
    borderStyle: 'dashed',
  },
  textoPuntoControl: {
    fontSize: 24,
    fontWeight: 'bold',
    color: COLORS.primaryText,
    textAlign: 'center',
  },
  subtextoPuntoControl: {
    fontSize: 14,
    fontWeight: 'bold',
    color: COLORS.primaryText,
    marginTop: 5,
  },
  navegacionCuadricula: {
    flex: 1,
    flexDirection: 'row',
    gap: 10,
  },
  tarjetaNav: {
    flex: 1,
    backgroundColor: COLORS.card,
    borderWidth: 1,
    borderColor: COLORS.border,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 10,
  },
  tituloNav: {
    color: COLORS.textHighlight,
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  estadoNav: {
    color: COLORS.success,
    fontSize: 10,
    letterSpacing: 1,
  }
});

export default PantallaPanelControl;