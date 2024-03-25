// Import React
import React from 'react';
// Import required components
import {StyleSheet} from 'react-native';
// Import Map and Marker
import {GooglePlacesAutocomplete} from 'react-native-google-places-autocomplete';

const SearchPlace = () => {
  return (
    <GooglePlacesAutocomplete
      minLength={2}
      placeholder="어디로 여행을 떠나시나요?"
      query={{
        key: 'AIzaSyDt8I7Ler2-Pq6lw-8KybDOMYelUsP_sZk',
        language: 'ko',
        components: 'country:kr',
      }}
      keyboardShouldPersistTaps={'handled'}
      fetchDetails={true}
      onPress={(data, details) => {
        console.log(data, details);
      }}
      onFail={error => console.log(error)}
      onNotFound={() => console.log('no results')}
      keepResultsAfterBlur={true}
      enablePoweredByContainer={false}
      styles={styles.search}
    />
  );
};
export default SearchPlace;

const styles = StyleSheet.create({
  search: {
    // container 감싸고 있는 컴포넌트
    container: {width: 370, marginTop: 20},
    // input을 감싸는 컴포넌트
    textInputContainer: {
      flexDirection: 'row',
    },
    // input창
    textInput: {
      borderRadius: 8,
      paddingVertical: 9,
      paddingHorizontal: 12,
      fontSize: 16,
      fontFamily: 'spoqaR',
      color: '#6c6c6e',
    },
    // 검색결과 리스트 컴포넌트
    listView: {
      backgroundColor: '#ffffff',
      borderRadius: 10,
      paddingHorizontal: 10,
      elevation: 8,
      shadowColor: '#6164BB',
    },
    // 검색결과 행
    row: {
      paddingVertical: 20,
    },
    // 검색결과 divided line
    separator: {
      height: 2,
      backgroundColor: '#c8c7cc',
    },
    // 검색결과 text
    description: {
      fontSize: 15,
      fontFamily: 'spoqaR',
    },
    // 필요없음
    loader: {
      flexDirection: 'row',
      justifyContent: 'flex-end',
      height: 20,
    },
  },
});
