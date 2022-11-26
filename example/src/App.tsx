import * as React from 'react';

import { StyleSheet, View, Text, TouchableOpacity, Image } from 'react-native';
import { init, open, isPreLoginResultValid } from 'react-native-geyan';
const logo = require('./assets/logo.png');

export default function App() {
  const [token, setToken] = React.useState('');
  const resolved = Image.resolveAssetSource(logo);
  async function handleOpenActivity() {
    try {
      const currentToken = await open({
        logo: resolved.uri,
        privacy: [
          { text: '洪九果品一键登录协议', url: 'https://www.hjfruit.com' },
        ],
      });
      setToken(currentToken);
    } catch (error) {
      console.log(error);
    }
  }
  React.useEffect(() => {
    init()
      .then(() => {
        console.log('预登录成功！');
      })
      .catch((err) => {
        console.log('初始化失败！', err);
      });
  }, []);
  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={handleOpenActivity}>
        <Text>Open Activity</Text>
      </TouchableOpacity>
      <Text>Token is: {token}</Text>
      <Text>valid?: {isPreLoginResultValid()}</Text>
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
