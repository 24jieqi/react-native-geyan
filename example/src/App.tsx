import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity, Image } from 'react-native';
import { multiply, showActivity } from 'react-native-geyan';
const logo = require('./assets/logo.png');

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();
  const [token, setToken] = React.useState('');
  React.useEffect(() => {
    multiply(3, 7).then(setResult);
  }, []);
  const resolved = Image.resolveAssetSource(logo);
  async function handleOpenActivity() {
    try {
      const currentToken = await showActivity({ logo: resolved.uri });
      setToken(currentToken);
    } catch (error) {}
  }
  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <TouchableOpacity onPress={handleOpenActivity}>
        <Text>Open Activity</Text>
      </TouchableOpacity>
      <Text>Token is: {token}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
