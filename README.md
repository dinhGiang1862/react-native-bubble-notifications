# react-native-bubble-notifications

external draw over app bubble for android

## Installation

```sh
npm install react-native-bubble-notifications
```

## Usage

Note this package is a React native wrapper utilizing Bubbles-for-android, the primary usage is to have the draw over other apps bubble that can also expand into a bigger view for large notification outside the app much like facebook chatheads. changing bubble image can be done by changing the icon in your andriod -> src -> main -> drawable and the view that it expands can be altered under android -> src -> main -> layout -> bubble_layout.xml file

```js
import {showBubble, hideBubble, requestBubblePermissions, checkBubblePermissions, initializeBubble, loadData} from 'react-native-bubble-notifications';

// ...
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
