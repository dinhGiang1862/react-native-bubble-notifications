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

export const loadData = (
  dropOffLoc,
  dropOffAddr,
  pickUpLoc,
  pickUpAddr,
  fare
) =>
  BubbleNotifications.loadData(
    dropOffLoc,
    dropOffAddr,
    pickUpLoc,
    pickUpAddr,
    fare
  );

export const expandNotification = (
  dropOffLoc,
  dropOffAddr,
  pickUpLoc,
  pickUpAddr,
  fare
) =>
  BubbleNotifications.expandNotification(
    dropOffLoc,
    dropOffAddr,
    pickUpLoc,
    pickUpAddr,
    fare
  );

export default {
  initializeBubble,
  requestBubblePermissions,
  checkBubblePermissions,
  hideBubble,
  showBubble,
  reopenApp,
  expandNotification,
  loadData,
};
