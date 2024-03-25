import React from 'react';
import {Dimensions} from 'react-native';
import {Wrapper} from './TravelStyle';
import {useAppDispatch} from '../../store/hooks';
import {useFocusEffect} from '@react-navigation/native';
import {setDisplay} from '../../store/slices/tabState';
import Plans from '../../components/travel/Plans';
import Package from '../../components/travel/Package';
import Icons from '../../components/travel/Icons';
import {PAGES} from '../../assets/data/pages';

function Travel() {
  const screenWidth = Math.round(Dimensions.get('window').width);
  const dispatch = useAppDispatch();

  useFocusEffect(() => {
    dispatch(setDisplay(true));
  });

  return (
    <Wrapper>
      <Plans />
      <Icons pages={PAGES} gap={25} offset={35} pageWidth={screenWidth - 51} />
      <Package
        pages={PAGES}
        gap={15}
        offset={35}
        pageWidth={screenWidth - (10 + 36) * 4.7}
      />
    </Wrapper>
  );
}

export default Travel;
