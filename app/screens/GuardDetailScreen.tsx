import React from 'react';
import { View, Text, StyleSheet, ScrollView, FlatList, TouchableOpacity } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { COLORS } from '../../constants/colors';

const LOG_DATA = [
  { id: '1', type: 'CHECKPOINT DELTA', time: '23:40', status: 'OK' },
  { id: '2', type: 'CHECKPOINT ECHO', time: '23:25', status: 'OK' },
  { id: '3', type: 'CHECKPOINT FOXTROT', time: '23:10', status: 'OK' },
];

const GuardDetailScreen = () => {
  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.screenTitle}>GUARD DETAIL PANEL</Text>
      </View>

      <View style={styles.infoGrid}>
        <View style={styles.infoBox}>
          <Text style={styles.label}>ID</Text>
          <Text style={styles.value}>G002</Text>
        </View>
        <View style={styles.infoBox}>
          <Text style={styles.label}>NAME</Text>
          <Text style={styles.value}>RODRÍGUEZ, A.</Text>
        </View>
        <View style={styles.infoBox}>
          <Text style={styles.label}>SHIFT START</Text>
          <Text style={styles.value}>22:00</Text>
        </View>
        <View style={styles.infoBox}>
          <Text style={styles.label}>LAST MOV.</Text>
          <Text style={[styles.value, { color: COLORS.primary }]}>30s INACTIVITY</Text>
        </View>
      </View>

      <View style={styles.logSection}>
        <Text style={styles.sectionHeader}>CHECKPOINT LOG</Text>
        
        <FlatList
          data={LOG_DATA}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <View style={styles.logItem}>
              <View style={styles.logIndicator} />
              <View style={{ flex: 1 }}>
                <Text style={styles.logType}>{item.type}</Text>
              </View>
              <Text style={styles.logTime}>{item.time}</Text>
            </View>
          )}
        />
      </View>

      <TouchableOpacity style={styles.closeButton}>
        <Text style={styles.closeButtonText}>CLOSE PANEL</Text>
      </TouchableOpacity>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: COLORS.background,
    padding: 10,
  },
  header: {
    paddingVertical: 15,
    borderBottomWidth: 1,
    borderBottomColor: COLORS.border,
    marginBottom: 10,
  },
  screenTitle: {
    color: COLORS.primary,
    fontSize: 16,
    fontWeight: 'bold',
    letterSpacing: 1,
  },
  infoGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    marginBottom: 20,
  },
  infoBox: {
    width: '48%',
    backgroundColor: 'transparent',
    borderWidth: 1,
    borderColor: COLORS.text,
    padding: 10,
    marginBottom: 10,
  },
  label: {
    color: COLORS.primary,
    fontSize: 10,
    marginBottom: 5,
  },
  value: {
    color: COLORS.textHighlight,
    fontSize: 14,
    fontWeight: 'bold',
  },
  logSection: {
    flex: 1,
    borderWidth: 1,
    borderColor: COLORS.border,
    padding: 10,
  },
  sectionHeader: {
    color: COLORS.primary,
    marginBottom: 10,
    fontSize: 12,
  },
  logItem: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#000',
    marginBottom: 5,
    padding: 10,
    borderLeftWidth: 3,
    borderLeftColor: COLORS.success,
  },
  logIndicator: {
    width: 8,
    height: 8,
    backgroundColor: COLORS.success,
    marginRight: 10,
  },
  logType: {
    color: COLORS.text,
    fontSize: 12,
  },
  logTime: {
    color: COLORS.textHighlight,
    fontSize: 12,
    fontFamily: 'monospace',
  },
  closeButton: {
    backgroundColor: COLORS.primary,
    padding: 15,
    marginTop: 10,
    alignItems: 'center',
  },
  closeButtonText: {
    color: 'black',
    fontWeight: 'bold',
  }
});

export default GuardDetailScreen;