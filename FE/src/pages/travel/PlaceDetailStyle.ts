import {TouchableOpacity} from 'react-native';
import styled from 'styled-components/native';
import {font_dark, primary} from '../../constants/colors';

const Container = styled.View`
  flex: 1;
  background-color: #fff;
`;

const ImageBackground = styled.Image`
  width: 100%;
  height: 200px;
`;

const DetailsContainer = styled.View`
  padding: 20px;
  padding-top: 0;
`;

const HeadersContainer = styled.View`
  padding: 20px;
  flex-direction: row;
  justify-content: space-between;
`;

const Header = styled.View``;

const Line = styled.View`
  width: 100%;
  border-width: 0.5px;
  border-style: solid;
  border-color: ${font_dark};
  margin-bottom: 8px;
`;

const Title = styled.Text`
  font-size: 24px;
  font-weight: 900;
`;

const Address = styled.Text`
  width: 300px;
`;

const Bag = styled(TouchableOpacity)`
  width: 60px;
  height: 60px;
  border-radius: 50px;
  background-color: white;
  justify-content: center;
  align-items: center;
  // background-color: ${primary};
`;

const BagImage = styled.Image`
  width: 60px;
  height: 60px;
  overflow: hidden;
`;

const StarInfo = styled.View`
  flex-direction: row;
  align-items: center;
`;

const Info = styled.Text`
  font-size: 15px;
  margin-top: 5px;
  margin-bottom: 5px;
`;

const ReviewsContainer = styled.View`
  margin-top: 15px;
`;

const ReviewItem = styled.View`
  flex-direction: row;
  align-items: center;
  margin-bottom: 10px;
  width: 330px;
  border: 1px solid ${font_dark};
  border-radius: 10px;
  padding: 12px;
`;

const ReviewImage = styled.Image`
  width: 100px;
  height: 100px;
  margin-right: 10px;
`;

const ProfileImage = styled.Image`
  width: 50px;
  height: 50px;
  border-radius: 25px;
  margin-right: 20px;
`;

const ReviewDetails = styled.View`
  flex: 1;
`;

const ReviewWriter = styled.Text`
  font-size: 16px;
  font-weight: bold;
`;

const ReviewRating = styled.Text`
  font-size: 14px;
  color: green;
`;

const HeaderContainer = styled.View`
  margin-top: 15px;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`;

const ReviewContent = styled.Text`
  font-size: 14px;
`;

const NavigationButtons = styled.View`
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
`;

const NavButton = styled.Text`
  font-size: 35px;
  margin-right: 10px;
`;
export {
  Container,
  ImageBackground,
  DetailsContainer,
  HeadersContainer,
  Header,
  Title,
  Address,
  StarInfo,
  Line,
  Info,
  Bag,
  BagImage,
  ReviewsContainer,
  ReviewItem,
  ReviewImage,
  ProfileImage,
  ReviewDetails,
  ReviewWriter,
  ReviewRating,
  HeaderContainer,
  ReviewContent,
  NavigationButtons,
  NavButton,
};
