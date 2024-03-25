import styled from 'styled-components/native';
import {bg_light, bg_lightgray} from '../../constants/colors';
import {TouchableOpacity} from 'react-native';

const Container = styled.View`
  flex: 1;
  position: relative;
  top: 0px;
  left: 0px;
  right: 0px;
  bottom: 0px;
  align-items: center;
  justify-content: center;
`;

const SliderContainer = styled.View`
  position: relative;
  width: 100%;
  height: 150px;
  background-color: ${bg_light};
  align-items: center;
  justify-content: center;
  border-top-right-radius: 15px;
  border-top-left-radius: 15px;
`;

const DragBar = styled(TouchableOpacity)`
  position: absolute;
  top: 0px;
  width: 100%;
  height: 25px;
  border: 2px solid ${bg_lightgray};
  border-top-right-radius: 15px;
  border-top-left-radius: 15px;
`;

const ButtonImage = styled.Image`
  width: 100%;
  height: 25px;
  resize-mode: contain;
`;

const PlanImage = styled.Image`
  width: 55px;
  height: 55px;
  resize-mode: contain;
`;

const SliderFullContent = styled.View`
  position: relative;
  flex: 1;
  width: 100%;
  margin-top: 60px;
  background-color: ${bg_light};
  align-items: center;
  justify-content: center;
  border-top-right-radius: 15px;
  border-top-left-radius: 15px;
`;

const PlanIcon = styled(TouchableOpacity)`
  position: absolute;
  background-color: ${bg_light};
  width: 45px;
  height: 45px;
  top: 80px;
  right: 20px;
  border-radius: 25px;
  align-items: center;
  justify-content: center;
`;

export {
  Container,
  SliderContainer,
  SliderFullContent,
  ButtonImage,
  PlanImage,
  PlanIcon,
  DragBar,
};
