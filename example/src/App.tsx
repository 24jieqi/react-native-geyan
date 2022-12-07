import * as React from 'react';

import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  StatusBar,
  Platform,
} from 'react-native';
import { init, open } from 'react-native-geyan';
const logo = require('./assets/logo.png');

export default function App() {
  const [token, setToken] = React.useState('');
  React.useEffect(() => {
    const entry = StatusBar.pushStackEntry({
      barStyle: 'dark-content',
      translucent: true,
    });
    Platform.OS === 'android' && StatusBar.setBackgroundColor('transparent');
    return () => {
      StatusBar.popStackEntry(entry);
    };
  });
  async function handleOpenActivity() {
    try {
      const currentToken = await open({
        logo,
      });
      setToken(currentToken);
    } catch (error) {
      console.log(error);
    }
  }
  React.useEffect(() => {
    init({
      appid: 'PxnDDshlyj7d4edorykj24',
    })
      .then(() => {
        console.log('初始化成功！');
      })
      .catch((err) => {
        console.log('初始化失败！', err);
      });
  }, []);
  return (
    <View style={styles.container}>
      <TouchableOpacity style={styles.btnWrapper} onPress={handleOpenActivity}>
        <Text style={styles.btnText}>一键登录</Text>
      </TouchableOpacity>
      <Text style={styles.resultText}>Token is: {token}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 12,
  },
  btnWrapper: {
    backgroundColor: '#0065fe',
    padding: 12,
    borderRadius: 5,
  },
  btnText: {
    textAlign: 'center',
    color: '#ffffff',
  },
  resultText: {
    marginTop: 8,
  },
});
