# react-native-geyan

个推[“个验·一键认证”](https://docs.getui.com/geyan/elogin/)React-Native 原生模块

## Installation

```sh
yarn add react-native-geyan
```

## Setup

> 在使用此模块前请先在个推管理后台完成应用创建

### Android

编辑`android/app/build.gradle`，添加`appId`和`channel`

```gradle
android {
  ...
  defaultConfig {
    ...
    manifestPlaceholders = [
      GETUI_APPID: "your app id",
      GT_INSTALL_CHANNEL: "getui" // your channel
    ]
  }
}
```

确保`applicationId`与应用后台 Android 包名一致
确保后台填写的**Android 签名(MD5)**和应用使用的`keystore`文件保持一致

### iOS

确保`Bundle Identifier`和个验后台配置一致

## Usage

```js
import { open, init, close, isPreLoginResultValid } from 'react-native-geyan';
const logo = require('./assets/logo.png');

init({
  appid: 'your appid',
});

async function startELogin() {
  if (!isPreLoginResultValid()) {
    return;
  }
  try {
    const token = await open({
      logo,
    });
    console.log(token: `${token}`)
  } catch(e) {
    // ...
  }
  // iOS only
  await close();
}
```

> iOS 需要手动调用`close`关闭一键登录页面

更详细的使用方式见`example`

## API

### `init(config: GeyanInitConfig): Promise<string>`

```ts
interface GeyanInitConfig {
  /**
   * 渠道（Android only）
   */
  channel?: string;
  /**
   * appid（iOS only）
   */
  appid: string;
}
```

初始化 SDK 并进行预登录

### `open(config: GeyanConfig): Promise<GeyanResult>`

```ts
interface GeyanConfig {
  /**
   * 应用图标（用于一键登录页logo露出）
   */
  logo: ImageSourcePropType;
  /**
   * 自定义的隐私策略 (Android only) (若无请传空数组)
   */
  privacy?: PrivacyItem[];
}
```

打开一键登录页，成功后返回`token`

### `close(): Promise`

关闭一键登录页面

> 仅 iOS 支持

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
