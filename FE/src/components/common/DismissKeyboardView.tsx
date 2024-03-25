import React from 'react';
import {
  TouchableWithoutFeedback,
  Keyboard,
  StyleSheet,
  View,
} from 'react-native';
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scrollview';

const DismissKeyboardView = ({children, style, ...props}: any) => (
  <TouchableWithoutFeedback onPress={Keyboard.dismiss} accessible={false}>
    <View style={[styles.container, style]}>
      <KeyboardAwareScrollView
        {...props}
        contentContainerStyle={styles.scrollViewContent}>
        {children}
      </KeyboardAwareScrollView>
    </View>
  </TouchableWithoutFeedback>
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  scrollViewContent: {
    flexGrow: 1,
  },
});

export default DismissKeyboardView;
