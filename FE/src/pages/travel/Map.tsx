import React from 'react';
import {SafeAreaView} from 'react-native';
import {NavigationProp, useNavigation} from '@react-navigation/native';
import GoogleMap from '../../components/travel/GoogleMap';
import SearchPlace from '../../components/travel/SearchPlace';
import {MapStackParams} from '../../interfaces/router/MapStackParams';
import {Container, PlanImage, PlanIcon} from './MapStyle';
import PlaceInfo from '../../components/travel/PlaceInfo';

const Map = () => {
  const navigation = useNavigation<NavigationProp<MapStackParams>>();

  const handlePlanPress = () => {
    navigation.navigate('plandetail');
  };

  return (
    <SafeAreaView style={{flex: 1}}>
      <Container>
        <GoogleMap />
        <SearchPlace />
        <PlanIcon onPress={handlePlanPress}>
          <PlanImage source={require('../../assets/images/planning.png')} />
        </PlanIcon>
        <PlaceInfo />
      </Container>
    </SafeAreaView>
  );
};
export default Map;
