import React, {useState} from 'react';
import {
  Wrapper,
  TitleContainer,
  TitleInput,
  TitleLength,
  TitleLengthText,
} from './TripTitleStyle';
import {NavigationProp, useNavigation} from '@react-navigation/native';
import {TripTitleStackParams} from '../../interfaces/router/TripTitleStackParams';
import AppButton from '../../components/common/AppButton';
import {BottomButton} from '../../constants/AppButton';
import DismissKeyboardView from '../../components/common/DismissKeyboardView';
import {StyleSheet} from 'react-native';
import {useAppDispatch} from '../../store/hooks';
import {setTripTitle} from '../../store/slices/trip';

const TripTitle = () => {
  const navigation = useNavigation<NavigationProp<TripTitleStackParams>>();
  const dispatch = useAppDispatch();
  const [title, setTitle] = useState('오희주님의 프랑스 여행 계획');

  const handleSubmit = () => {
    navigation.navigate('map', {title});
    dispatch(setTripTitle(title));
  };

  return (
    <DismissKeyboardView style={Style.container}>
      <Wrapper>
        <TitleContainer>
          <TitleInput
            placeholder="오희주님의 프랑스 여행 계획"
            value={title}
            onChangeText={setTitle}
          />
          <TitleLength>
            <TitleLengthText>{title.length}/15</TitleLengthText>
          </TitleLength>
        </TitleContainer>
        <AppButton style={BottomButton} text="다음" onPress={handleSubmit} />
      </Wrapper>
    </DismissKeyboardView>
  );
};

const Style = StyleSheet.create({
  container: {flex: 1},
});

export default TripTitle;
