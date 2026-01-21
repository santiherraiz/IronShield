import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity, StyleSheet, StatusBar, Alert } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';
import { useLogin } from '../../hooks/useLogin';

const PantallaLogin = () => {

  const { idServicio, setIdServicio, contrasena, setContrasena, gestionarInicioSesion } = useLogin();

  return (
    <SafeAreaView style={styles.contenedor}>
      <StatusBar barStyle="light-content" backgroundColor={COLORS.background} />

      <View style={styles.encabezado}>
        <Text style={styles.titulo}>TRACKMATE</Text>
        <Text style={styles.subtitulo}>OPERACIONES DE SEGURIDAD</Text>
      </View>

      <View style={styles.contenedorFormulario}>
        <Text style={styles.etiqueta}>ID DE SERVICIO</Text>
        <TextInput
          style={styles.entrada}
          placeholder="S001 o G001"
          placeholderTextColor="#546E7A"
          value={idServicio}
          onChangeText={setIdServicio}
          autoCapitalize="characters"
        />

        <Text style={styles.etiqueta}>CONTRASEÑA</Text>
        <TextInput
          style={styles.entrada}
          placeholder="••••••"
          placeholderTextColor="#546E7A"
          value={contrasena}
          onChangeText={setContrasena}
          secureTextEntry
        />

        <TouchableOpacity style={styles.boton} onPress={gestionarInicioSesion}>
          <Text style={styles.textoBoton}>ACCEDER AL SISTEMA</Text>
        </TouchableOpacity>

        <Text style={styles.textoPie}>SOLO PERSONAL AUTORIZADO</Text>
        <Text style={styles.textoVersion}>SIS v0.0.3</Text>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  contenedor: {
    flex: 1,
    backgroundColor: COLORS.background,
    padding: 20,
    justifyContent: 'center',
  },
  encabezado: {
    marginBottom: 50,
    alignItems: 'center',
  },
  titulo: {
    fontSize: 32,
    fontWeight: 'bold',
    color: COLORS.primary,
    letterSpacing: 2,
  },
  subtitulo: {
    fontSize: 16,
    color: COLORS.text,
    letterSpacing: 4,
    marginTop: 5,
  },
  contenedorFormulario: {
    width: '100%',
  },
  etiqueta: {
    color: COLORS.text,
    fontSize: 12,
    fontWeight: 'bold',
    marginBottom: 8,
    letterSpacing: 1,
  },
  entrada: {
    backgroundColor: COLORS.card,
    borderWidth: 1,
    borderColor: COLORS.border,
    color: COLORS.textHighlight,
    padding: 15,
    marginBottom: 20,
    fontSize: 16,
    fontFamily: 'monospace',
  },
  boton: {
    backgroundColor: COLORS.primary,
    padding: 18,
    alignItems: 'center',
    marginTop: 20,
    borderWidth: 1,
    borderColor: COLORS.primary,
  },
  textoBoton: {
    color: COLORS.primaryText,
    fontSize: 18,
    fontWeight: 'bold',
    letterSpacing: 1,
  },
  textoPie: {
    color: COLORS.danger,
    textAlign: 'center',
    marginTop: 40,
    fontSize: 12,
    letterSpacing: 1,
  },
  textoVersion: {
    color: COLORS.text,
    textAlign: 'center',
    marginTop: 10,
    fontSize: 10,
    opacity: 0.5,
  },
});

export default PantallaLogin;