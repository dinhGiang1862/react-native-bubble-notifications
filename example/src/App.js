import * as React from 'react';

import {
  StyleSheet,
  View,
  Button,
  Text,
  ToastAndroid,
  DeviceEventEmitter,
} from 'react-native';

import {
  initializeBubble,
  requestBubblePermissions,
  checkBubblePermissions,
  hideBubble,
  showBubble,
  loadData,
} from '../NativeModules/index.js';

const showToast = (text) => ToastAndroid.show(text, 1000);

export default function App() {
  const onAdd = () => showBubble().then(() => showToast('Add Floating Button'));
  const onHide = () =>
    hideBubble()
      .then(() => showToast('Manually Removed Bubble'))
      .catch(() => showToast('Failed to remove'));
  const onRequestPermission = () =>
    requestBubblePermissions()
      .then(() => showToast('Permission received'))
      .catch(() => showToast('Failed to get permission'));
  const onCheckPermissoin = () =>
    checkBubblePermissions()
      .then((value) => showToast(`Permission: ${value ? 'Yes' : 'No'}`))
      .catch(() => showToast('Failed to check'));
  const onInit = () =>
    initializeBubble()
      .then(() => showToast('Init'))
      .catch(() => showToast('Failed init'));
  const expandWithData = () => {
    loadData(
      '15 min / 5 Mile trip',
      'Mojo Coffee, 620S, Cedar Park',
      '8 min / 3 miles away',
      '1413 Colorado Bend dr Cedar Park',
      '$10.75'
    );
  };

  React.useEffect(() => {
    const subscriptionPress = DeviceEventEmitter.addListener(
      'floating-bubble-press',
      () => {
        // reopenApp();
      }
    );
    const subscriptionRemove = DeviceEventEmitter.addListener(
      'floating-bubble-remove',
      () => {
        showToast('Remove Bubble');
      }
    );
    return () => {
      subscriptionPress.remove();
      subscriptionRemove.remove();
    };
  }, []);

  return (
    <View style={styles.container}>
      <Text>Check Permission</Text>
      <Button title="Check" onPress={onCheckPermissoin} />
      <Text>Get Permission</Text>
      <Button title="Get Permission" onPress={onRequestPermission} />
      <Text>Initialize Bubble Manage</Text>
      <Button title="Initialize" onPress={onInit} />
      <Text>Add the bubble</Text>
      <Button title="Add Bubble" onPress={onAdd} />
      <Text>Expand Container</Text>
      <Button title="load Data" onPress={expandWithData} />
      <Text>Remove the bubble</Text>
      <Button title="Hide Bubble" onPress={onHide} />
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
