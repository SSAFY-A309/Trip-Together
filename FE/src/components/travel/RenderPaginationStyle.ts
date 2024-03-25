import styled from 'styled-components/native';
import {bg_light, primary} from '../../constants/colors';
import {TouchableOpacity} from 'react-native';

const PaginationStyle = styled.View`
  position: absolute;
  width: 100%;
  background-color: ${bg_light};
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`;

const PaginationContainer = styled(TouchableOpacity)`
  width: 120px;
  height: 60px;
  background-color: ${bg_light};
  border-radius: 10px;
  justify-content: center;
  align-items: center;
  flex-direction: row;
`;

const NavContainer = styled.View`
  width: 80px;
  flex-direction: row;
  justify-content: space-around;
`;

const NavButton = styled(TouchableOpacity)``;

const NavText1 = styled.Text`
  font-size: 18px;
  color: ${primary};
`;

const NavText2 = styled.Text`
  font-size: 18px;
`;

const PaginationText = styled.Text`
  font-size: 20px;
  font-weight: bold;
`;

const DownImage = styled.Image`
  width: 20px;
  height: 20px;
  margin-left: 10px;
`;

const CenteredView = styled.View`
  flex: 1;
  justify-content: center;
  align-items: center;
  margin-top: 50px;
  width: 100%;
  height: 90%;
`;

const ModalView = styled.View`
  width: 100%;
  height: 97%;
  margin: 20px;
  background-color: white;
  border-radius: 20px;
  padding: 35px;
  align-items: center;
  shadow-color: #000;
  shadow-offset: {
    width: 0;
    height: 2;
  }
  shadow-opacity: 0.25;
  shadow-radius: 4px;
  elevation: 5;
`;

const Item = styled.TouchableOpacity`
  padding: 10px;
  border-bottom-width: 1px;
  border-bottom-color: #ccc;
  width: 100%;
  align-items: center;
`;

const ItemText = styled.Text`
  font-size: 18px;
`;

export {
  PaginationStyle,
  PaginationContainer,
  NavContainer,
  NavButton,
  NavText1,
  NavText2,
  PaginationText,
  DownImage,
  CenteredView,
  ModalView,
  Item,
  ItemText,
};
