import styled from 'styled-components/native';
import {bg_light, bg_lightgray} from '../../constants/colors';

const Wrapper = styled.View`
  flex: 1;
  background-color: ${bg_light};
  width: 100%;
  height: 100%;
  justify-content: space-between;
  align-items: center;
`;

const TitleContainer = styled.View`
  height: 50px;
  width: 90%;
  margin-top: 250px;
  justify-content: center;
  align-items: center;
`;

const TitleInput = styled.TextInput`
  width: 80%;
  padding: 1px 15px;
  height: 40px;
  border-bottom-width: 1px;
  border-bottom-color: ${bg_lightgray};
`;

const TitleLength = styled.View`
  width: 80%;
  flex-direction: row;
  justify-content: flex-end;
`;

const TitleLengthText = styled.Text`
  font-size: 12px;
  margin-right: 8px;
`;

export {Wrapper, TitleContainer, TitleInput, TitleLength, TitleLengthText};
