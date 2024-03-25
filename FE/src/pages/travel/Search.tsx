import React from 'react';
import {Wrapper, SearchInput} from './SearchStyle';
import {NavigationProp, useNavigation} from '@react-navigation/native';
import {SearchStackParams} from '../../interfaces/router/SearchStackParams';
import {useAppDispatch} from '../../store/hooks';
import {useFocusEffect} from '@react-navigation/native';
import {setDisplay} from '../../store/slices/tabState';
import DismissKeyboardView from '../../components/common/DismissKeyboardView';
import {StyleSheet} from 'react-native';

const Search = () => {
  const dispatch = useAppDispatch();
  const navigation = useNavigation<NavigationProp<SearchStackParams>>();

  useFocusEffect(() => {
    dispatch(setDisplay(false));
  });

  const handleSearchSubmit = () => {
    navigation.navigate('calendar');
  };

  return (
    <DismissKeyboardView style={styles.dismissKeyboard}>
      <Wrapper>
        <SearchInput
          placeholder="어디로 여행을 떠나시나요?"
          // placeholderTextColor={font_light}
          onSubmitEditing={handleSearchSubmit}
        />
      </Wrapper>
    </DismissKeyboardView>
  );
};

const styles = StyleSheet.create({
  dismissKeyboard: {
    backgroundColor: 'white',
    padding: 20,
  },
});

export default Search;
