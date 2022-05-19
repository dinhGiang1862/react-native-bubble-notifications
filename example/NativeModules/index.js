import { NativeModules } from 'react-native';

const { BubbleNotifications } = NativeModules;

export const reopenApp = () => BubbleNotifications.reopenApp();
export const showBubble = (x = 50, y = 100) =>
  BubbleNotifications.showFloatingBubble(x, y);
export const hideBubble = () => BubbleNotifications.hideFloatingBubble();
export const checkBubblePermissions = () =>
  BubbleNotifications.checkPermission();
export const requestBubblePermissions = () =>
  BubbleNotifications.requestPermission();
export const initializeBubble = () => BubbleNotifications.initialize();

export default {
  initializeBubble,
  requestBubblePermissions,
  checkBubblePermissions,
  hideBubble,
  showBubble,
  reopenApp,
};
