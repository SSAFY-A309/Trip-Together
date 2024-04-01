import React, {useState} from 'react';
import DateTimePicker from 'react-native-modal-datetime-picker';
import {Picker} from '@react-native-picker/picker';
import {
  Wrapper,
  Box,
  InputLabel,
  InputField,
  DateTimePickerText,
  PickerContainer,
  StyledPicker,
  SideBox,
} from './MakeFlashStyle';
import {useAppDispatch} from '../../store/hooks';
import {setDisplay} from '../../store/slices/tabState';
import {NavigationProp, useFocusEffect} from '@react-navigation/native';
import AppButton from '../../components/common/AppButton';
import {BottomButton} from '../../constants/AppButton';
import DismissKeyboardView from '../../components/common/DismissKeyboardView';
import {useNavigation} from '@react-navigation/native';
import {FlashMainStackParams} from '../../interfaces/router/flashMob/FlashMainStackParams';

const MakeFlash = () => {
  const [title, setTitle] = useState('');
  const [startTime, setStartTime] = useState<Date | null>(null);
  const [isDateTimePickerVisible, setIsDateTimePickerVisible] = useState(false);
  const [numParticipants, setNumParticipants] = useState('');
  const dispatch = useAppDispatch();
  const navigation = useNavigation<NavigationProp<FlashMainStackParams>>();

  useFocusEffect(() => {
    dispatch(setDisplay(false));
  });

  const showDateTimePicker = () => {
    setIsDateTimePickerVisible(true);
  };

  const hideDateTimePicker = () => {
    setIsDateTimePickerVisible(false);
  };

  const handleDateConfirm = (date: any) => {
    setStartTime(date);
    hideDateTimePicker();
  };

  const handleSearchSubmit = () => {
    navigation.navigate('allflash');
  };

  return (
    <DismissKeyboardView>
      <Wrapper>
        <SideBox />
        <Box>
          <InputLabel>제목</InputLabel>
          <InputField
            value={title}
            onChangeText={setTitle}
            placeholder="제목을 입력하세요"
          />
        </Box>

        <Box>
          <InputLabel>시작 시간</InputLabel>
          <DateTimePickerText onPress={showDateTimePicker}>
            {startTime ? startTime.toLocaleString() : '시작 시간을 선택하세요'}
          </DateTimePickerText>
          <DateTimePicker
            isVisible={isDateTimePickerVisible}
            mode="datetime"
            onConfirm={handleDateConfirm}
            onCancel={hideDateTimePicker}
          />
        </Box>

        <Box>
          <InputLabel>모집 인원</InputLabel>
          <PickerContainer>
            <StyledPicker
              selectedValue={numParticipants}
              onValueChange={(itemValue: string) =>
                setNumParticipants(itemValue)
              }>
              <Picker.Item label="1명" value="1" />
              <Picker.Item label="2명" value="2" />
              <Picker.Item label="3명" value="3" />
              <Picker.Item label="4명" value="4" />
              <Picker.Item label="5명" value="5" />
              <Picker.Item label="6명" value="6" />
              <Picker.Item label="7명" value="7" />
              <Picker.Item label="8명" value="8" />
              <Picker.Item label="9명" value="9" />
              <Picker.Item label="10명" value="10" />
            </StyledPicker>
          </PickerContainer>
        </Box>
        <SideBox />
        <AppButton
          style={BottomButton}
          text="다음"
          onPress={handleSearchSubmit}
        />
      </Wrapper>
    </DismissKeyboardView>
  );
};

export default MakeFlash;