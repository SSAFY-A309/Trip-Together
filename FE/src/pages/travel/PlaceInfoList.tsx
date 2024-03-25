import React from 'react';
import {FlatList} from 'react-native';
import Places from '../../assets/data/place';
import {NavigationProp, useNavigation} from '@react-navigation/native';
import {PlaceStackParams} from '../../interfaces/router/PlaceStackParams';
import {StarRatingDisplay} from 'react-native-star-rating-widget';
import {
  ItemContainer,
  Thumbnail,
  DetailsContainer,
  StarContainer,
  Name,
  Address,
  Rating,
  Price,
  ThumbnailContainer,
} from './PlaceInfoListStyle';

const PlaceInfoList = () => {
  const navigation = useNavigation<NavigationProp<PlaceStackParams>>();

  const handlePress = () => {
    navigation.navigate('placedetail');
  };

  const renderItem = ({item}: any) => (
    <ItemContainer onPress={handlePress}>
      <ThumbnailContainer>
        <Thumbnail
          source={require('../../assets/images/sagradafamilia.png')}
          resizeMode="contain"
        />
      </ThumbnailContainer>
      <DetailsContainer>
        <Name>{item.name}</Name>
        <Address>{item.address}</Address>
        <StarContainer>
          <Rating>평점: {item.avg_rating}</Rating>
          <StarRatingDisplay rating={item.avg_rating} starSize={20} />
        </StarContainer>
        <Price>평균 가격: {item.avg_price}</Price>
      </DetailsContainer>
    </ItemContainer>
  );

  return (
    <FlatList
      data={Places}
      renderItem={renderItem}
      keyExtractor={item => item.attraction_id.toString()}
    />
  );
};

export default PlaceInfoList;
