import React from 'react';
import { createStackNavigator } from '@react-navigation/stack';
import { StatusBar } from 'react-native';
import { COLORS } from '../../constants/colors';
import PantallaLogin from '../screens/LoginScreens';
import PantallaPanelControl from '../screens/DashboardScreen';
import PantallaMonitoreoMapa from '../screens/MapMonitorScreen';
import PantallaDetalleGuardia from '../screens/GuardDetailScreen';

const Stack = createStackNavigator();

const AppNavigator = () => {
  return (
    <>
      <StatusBar barStyle="light-content" backgroundColor={COLORS.background} />
      
      <Stack.Navigator
        id="MainStack"
        initialRouteName="Login"
        screenOptions={{
          headerShown: false,
          headerStyle: {
            backgroundColor: COLORS.background,
            borderBottomWidth: 1,
            borderBottomColor: COLORS.border,
            elevation: 0,
            shadowOpacity: 0,
          },
          headerTintColor: COLORS.primary,
          headerTitleStyle: {
            fontWeight: 'bold',
            letterSpacing: 1,
            fontSize: 16,
          },
          headerBackTitle: '',
          cardStyle: { backgroundColor: COLORS.background },
        }}
      >
        <Stack.Screen 
          name="Login" 
          component={PantallaLogin} 
          options={{ headerShown: false }}
        />

        <Stack.Screen 
          name="OperationsStack" 
          component={PantallaPanelControl} 
          options={{ 
            title: 'TRACKMATE // OPS',
            headerLeft: () => null,
            gestureEnabled: false, 
          }} 
        />

        <Stack.Screen 
          name="MapMonitor" 
          component={PantallaMonitoreoMapa} 
          options={{ title: 'MONITOR DE GEOLOCALIZACIÓN' }} 
        />

        <Stack.Screen 
          name="GuardDetail" 
          component={PantallaDetalleGuardia} 
          options={{ title: 'REGISTRO DE GUARDIA' }} 
        />

      </Stack.Navigator>
    </>
  );
};

export default AppNavigator;