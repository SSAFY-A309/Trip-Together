import styled from 'styled-components/native';
import {bg_main} from '../../constants/colors';

const Wrapper = styled.View`
  flex: 1;
  align-items: center;
  width: 100%;
`;

const DateContainer = styled.View`
  width: 80%;
  height: 40px;
  border-radius: 20px;
  background-color: ${bg_main};
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  margin-bottom: 20px;
`;

const DateText = styled.Text`
  font-size: 15px;
  width: 100px;
`;

export {Wrapper, DateContainer, DateText};
