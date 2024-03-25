import styled from 'styled-components/native';
import {View, Animated, Text, Dimensions, TouchableOpacity} from 'react-native';
import SortableList from 'react-native-sortable-list';
import {bg_light, bg_lightgray} from '../../constants/colors';

const PlaceImage = styled.Image`
  width: 25px;
  height: 25px;
  margin-right: 15px;
`;

const Button = styled(TouchableOpacity)`
  width: 34px;
  height: 34px;
  border-radius: 50px;
  margin-left: 5px;
  justify-content: center;
  align-items: center;
`;

const Container = styled(View)`
  flex: 1;
  justify-content: center;
  align-items: center;
  background-color: #eee;
  padding-top: 60px;
`;

const FirstHalf = styled(View)`
  flex: 1.5;
  overflow: hidden;
`;

const Middle = styled.View`
  flex: 0.15;
  flex-direction: row;
  align-items: center;
  justify-content: flex-end;
  width: 90%;
`;

const MiddleTitle = styled.Text`
  font-size: 11px;
  font-weight: 700;
  margin-right: 6px;
  margin-left: 6px;
`;

const MiddlePrice = styled.Text`
  font-weight: 900;
`;

const SecondHalf = styled(View)`
  position: relative;
  flex: 1;
  overflow: hidden;
  background: ${bg_light};
  padding-top: 20px;
  border-top-right-radius: 15px;
  border-top-left-radius: 15px;
  elevation: 15;
`;

const DragBar = styled.View`
  position: absolute;
  top: 8px;
  left: 35%;
  width: 30%;
  border: 2px solid ${bg_lightgray};
  border-radius: 5px;
`;

const List = styled(SortableList)`
  flex: 1;
`;

const window = Dimensions.get('window');

const ContentContainer = styled(View)`
  width: ${window.width}px;
  padding-horizontal: 0;
`;

const RowContainer = styled(Animated.View)`
  flex-direction: row;
  align-items: center;
  background-color: #fff;
  padding: 8px;
  margin-horizontal: 16px;
  margin-vertical: 9px;
  border-radius: 8px;
  shadow-color: #000;
  shadow-offset: {
    width: 0;
    height: 2;
  }
  shadow-opacity: 0.25;
  shadow-radius: 3.84px;
  elevation: 5;
`;

const Image = styled(PlaceImage)`
  width: 55px;
  height: 55px;
  border-radius: 50px;
  margin-right: 16px;
`;

const InfoContainer = styled(View)`
  flex: 1;
`;

const Name = styled(Text)`
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 4px;
`;

const Address = styled(Text)`
  font-size: 14px;
  margin-bottom: 4px;
`;

const RatingContainer = styled(View)`
  flex-direction: row;
  align-items: center;
  margin-bottom: 4px;
`;

const Rating = styled(Text)`
  font-size: 14px;
  color: green;
  margin-right: 5px;
`;

const Price = styled(Text)`
  font-size: 14px;
`;

export {
  PlaceImage,
  Container,
  FirstHalf,
  Button,
  Middle,
  MiddleTitle,
  MiddlePrice,
  SecondHalf,
  DragBar,
  List,
  ContentContainer,
  RowContainer,
  Image,
  InfoContainer,
  Name,
  Address,
  RatingContainer,
  Rating,
  Price,
};
