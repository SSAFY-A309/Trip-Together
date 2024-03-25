import {NavigationContainer} from '@react-navigation/native';
import * as React from 'react';
import RootNavigator from './stacks/RootNavigator';
import usePermissions from './hooks/usePermissions';

function AppInner() {
  usePermissions();

  return (
    <NavigationContainer>
      <RootNavigator />
    </NavigationContainer>
  );
}

export default AppInner;
