import React from 'react';
import styled from 'styled-components/native';
import {
  ViewStyle,
  Text,
  Image,
  TouchableOpacity,
  StyleSheet,
  View,
} from 'react-native';
import {bg_light} from '../../constants/colors';
import {NavigationProp, useNavigation} from '@react-navigation/native';
import {TravelStackParams} from '../../interfaces/router/TravelStackParams';

interface IPage {
  item: {num: number; color: string};
  style: ViewStyle;
}

const PageItem = styled.View<{color: string}>`
  justify-content: center;
  align-items: center;
  border-radius: 10px;
  flex-direction: row;
  padding-right: 20px;
`;

const IconBox = styled.View`
  align-items: center;
`;

const IconText = styled.Text`
  font-weight: bold;
`;

const styles = StyleSheet.create({
  iconContainer: {
    width: 60,
    height: 60,
    backgroundColor: bg_light,
    justifyContent: 'center',
    margin: 10,
    alignItems: 'center',
    borderRadius: 10,
    shadowColor: '#000',
    elevation: 9,
  },
});

const MainIcons = ({item, style}: IPage) => {
  const navigation = useNavigation<NavigationProp<TravelStackParams>>();

  return (
    <PageItem color={item.color} style={style}>
      {item.num === 1 ? (
        <IconBox>
          <TouchableOpacity
            onPress={() => {
              navigation.navigate('planning');
            }}
            style={styles.iconContainer}>
            <Image
              source={require('../../assets/images/trip.png')}
              style={{width: 40, height: 40}}
            />
          </TouchableOpacity>
          <IconText>여행</IconText>
        </IconBox>
      ) : (
        <TouchableOpacity
          onPress={() => {
            // Handle onPress
          }}
          style={styles.iconContainer}>
          <Text>1</Text>
        </TouchableOpacity>
      )}
      {item.num === 1 ? (
        <IconBox>
          <TouchableOpacity
            onPress={() => {
              // Handle onPress
            }}
            style={styles.iconContainer}>
            <Image
              source={require('../../assets/images/insurance.png')}
              style={{width: 40, height: 40}}
            />
          </TouchableOpacity>
          <IconText>보험</IconText>
        </IconBox>
      ) : (
        <TouchableOpacity
          onPress={() => {
            // Handle onPress
          }}
          style={styles.iconContainer}>
          <Text>1</Text>
        </TouchableOpacity>
      )}
      {item.num === 1 ? (
        <IconBox>
          <TouchableOpacity
            onPress={() => {
              // Handle onPress
            }}
            style={styles.iconContainer}>
            <Image
              source={require('../../assets/images/CurExchange.png')}
              style={{width: 40, height: 40}}
            />
          </TouchableOpacity>
          <IconText>환전</IconText>
        </IconBox>
      ) : (
        <TouchableOpacity
          onPress={() => {
            // Handle onPress
          }}
          style={styles.iconContainer}>
          <Text>1</Text>
        </TouchableOpacity>
      )}
      {item.num === 1 ? (
        <IconBox>
          <TouchableOpacity
            onPress={() => {
              // Handle onPress
            }}
            style={styles.iconContainer}>
            <Image
              source={require('../../assets/images/shopping.png')}
              style={{width: 40, height: 40}}
            />
          </TouchableOpacity>
          <IconText>상품</IconText>
        </IconBox>
      ) : (
        <TouchableOpacity
          onPress={() => {
            // Handle onPress
          }}
          style={styles.iconContainer}>
          <Text>1</Text>
        </TouchableOpacity>
      )}
    </PageItem>
  );
};

export default MainIcons;
