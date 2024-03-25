import styled from 'styled-components/native';
import {primary, bg_lightgray} from '../../constants/colors';

const Container = styled.View`
  height: 23%;
  border-radius: 10px;
  justify-content: center;
  align-items: center;
  background-color: white;
  margin: 10px 10px;
  padding: 10px 0px;
`;

const Indicator = styled.View<{focused: boolean}>`
  margin: 0px 4px;
  background-color: ${props => (props.focused ? primary : bg_lightgray)};
  width: 6px;
  height: 6px;
`;

const IndicatorWrapper = styled.View`
  flex-direction: row;
  align-items: center;
  margin-top: 16px;
`;

export {Container, Indicator, IndicatorWrapper};
