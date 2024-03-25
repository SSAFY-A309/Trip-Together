import {Shadow} from 'react-native-shadow-2';
import styled from 'styled-components/native';
import {
  bg_light,
  bg_lightgray,
  bg_main,
  font_dark,
} from '../../constants/colors';

const Wrapper = styled.View`
  width: 100%;
  background: ${bg_main};
  border-top-right-radius: 15px;
  border-top-left-radius: 15px;
`;

const StyledShadow = styled(Shadow)`
  align-self: stretch;
`;

const PlaceBox = styled.View`
  position: relative;
  background: ${bg_light};
  border-top-right-radius: 15px;
  border-top-left-radius: 15px;
  padding-top: 20px;
  height: 150px;
  width: 100%;
`;

const PlaceView = styled.View`
  flex-direction: row;
  align-items: center;
  padding: 10px 15px;
`;

const DragBar = styled.View`
  position: absolute;
  top: 8px;
  left: 35%;
  width: 30%;
  border: 2px solid ${bg_lightgray};
  border-radius: 5px;
`;

const PlaceImageView = styled.View`
  height: 80px;
  width: 80px;
  aspect-ratio: 1/1;
  align-items: center;
  justify-content: center;
  border-radius: 40px;
  overflow: hidden;
`;

const PlaceImage = styled.Image`
  width: 110px;
  height: 110px;
`;

const PlaceInfoView = styled.View`
  flex: 1;
  margin-left: 10px;
`;

const PlaceName = styled.Text`
  color: ${font_dark};
  font-weight: 600;
  font-size: 18px;
`;

const Address = styled.Text`
  font-size: 14px;
`;

const Description = styled.View`
  width: 100%;
  margin-top: 10px;
  flex-direction: row;
  align-items: center;
`;

const Rating = styled.Text`
  font-size: 20px;
  margin-right: 5px;
`;

const Price = styled.Text`
  margin-left: 15px;
  font-size: 15px;
`;

export {
  Description,
  DragBar,
  PlaceName,
  PlaceBox,
  PlaceImage,
  PlaceImageView,
  PlaceView,
  StyledShadow,
  PlaceInfoView,
  Address,
  Wrapper,
  Rating,
  Price,
};
