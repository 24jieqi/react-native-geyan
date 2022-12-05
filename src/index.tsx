import { NativeModules, Platform } from 'react-native';
const LINKING_ERROR =
  `The package 'react-native-geyan' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const Geyan = NativeModules.Geyan
  ? NativeModules.Geyan
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

interface PrivacyItem {
  text: string;
  url: string;
}
interface GeyanConfig {
  logo: string;
  privacy: PrivacyItem[];
}

interface GeyanInitConfig {
  /**
   * 渠道
   */
  channel?: string;
  appid?: string;
}

/**
 * 个验初始化（初始化SDK，预登录）
 * @param channel 渠道
 * @returns
 */
export function init(config: GeyanInitConfig): Promise<string> {
  return Geyan.init(config);
}
/**
 * 打开个验验证页面
 * @param config
 * @returns
 */
export function open(config: GeyanConfig): Promise<string> {
  return Geyan.open(config);
}

/**
 * 预登陆是否可用
 */
export function isPreLoginResultValid(): boolean {
  return Geyan.isPreLoginResultValid();
}
