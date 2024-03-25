import React, {useCallback, useEffect, useMemo, useRef} from 'react';
import {Animated, StyleSheet, Platform, Easing, Dimensions} from 'react-native';
import {StarRatingDisplay} from 'react-native-star-rating-widget';
import {
  DragBar,
  Container,
  FirstHalf,
  SecondHalf,
  List,
  Image,
  InfoContainer,
  Name,
  RatingContainer,
  Rating,
  Price,
  Middle,
  MiddleTitle,
  MiddlePrice,
  PlaceImage,
  Button,
} from './PlanDayStyle';
import {RootState} from '../../store';
import {useAppSelector} from '../../store/hooks';
import {useAppDispatch} from '../../store/hooks';
import {deleteItemFromBag, addItemToBag} from '../../store/slices/bag';
import {addDailyPlan, deleteDailyPlan} from '../../store/slices/trip';

const window = Dimensions.get('window');

interface SortableDataItem {
  attraction_id: number;
  thumbnail_image_url: string;
  name: string;
  address: string;
  avg_rating: string;
  avg_price: string;
}

interface SortableDownDataItem {
  attraction_id: number;
  thumbnail_image_url: string;
  name: string;
  address: string;
  avg_rating: string;
  avg_price: string;
}

function Row(props: any) {
  const {active, data, UporDown, onPressUp, onPressDown, onPressTrash} = props;

  const activeAnim = useRef(new Animated.Value(0));

  const style = useMemo(
    () => ({
      ...Platform.select({
        android: {
          transform: [
            {
              scale: activeAnim.current.interpolate({
                inputRange: [0, 1],
                outputRange: [1, 1.07],
              }),
            },
          ],
          elevation: activeAnim.current.interpolate({
            inputRange: [0, 1],
            outputRange: [2, 6],
          }),
        },
      }),
    }),
    [],
  );

  useEffect(() => {
    Animated.timing(activeAnim.current, {
      duration: 300,
      easing: Easing.bounce,
      toValue: Number(active),
      useNativeDriver: true,
    }).start();
  }, [active]);

  return (
    <Animated.View style={[styles.row, style]}>
      <PlaceImage
        source={require('../../assets/images/drag.png')}
        resizeMode="cover"
      />
      <Image
        source={require('../../assets/images/sagradafamilia.png')}
        resizeMode="cover"
      />
      <InfoContainer>
        <Name>{data.name}</Name>
        {/* <Address>{data.address}</Address> */}
        <RatingContainer>
          <Rating>{`${data.avg_rating}`}</Rating>
          <StarRatingDisplay rating={4.9} starSize={12} />
        </RatingContainer>
        <Price>{`평균 가격: ${data.avg_price}`}</Price>
      </InfoContainer>
      {UporDown === 'up' ? (
        <Button onPress={() => onPressDown(data)}>
          <PlaceImage
            source={require('../../assets/images/godown.png')}
            resizeMode="cover"
          />
        </Button>
      ) : (
        <Button onPress={() => onPressUp(data)}>
          <PlaceImage
            source={require('../../assets/images/goup.png')}
            resizeMode="cover"
          />
        </Button>
      )}
      {UporDown === 'up' ? (
        <Button onPress={() => onPressTrash(data)}>
          <PlaceImage
            source={require('../../assets/images/trash.png')}
            resizeMode="cover"
          />
        </Button>
      ) : (
        <Button onPress={() => onPressTrash(data)}>
          <PlaceImage
            source={require('../../assets/images/trash.png')}
            resizeMode="cover"
          />
        </Button>
      )}
    </Animated.View>
  );
}

interface RenderRowProp {
  data: any;
  active: boolean;
}

interface DailyPlan {
  attractions: any[];
  order: number;
  daily_estimated_budget: number;
}

const PlanDay = ({dailyPlan}: {dailyPlan: DailyPlan}) => {
  const topList = dailyPlan.attractions;
  const today = dailyPlan.order;
  const bottomList = useAppSelector((state: RootState) => state.bag.bagInfo);
  const dispatch = useAppDispatch();

  const handleRowPress = useCallback((row, action) => {
    if (action === 'up') {
      dispatch(addDailyPlan({order: today, attraction: row}));
      dispatch(deleteItemFromBag(row.attraction_id));
    } else if (action === 'down') {
      dispatch(
        deleteDailyPlan({order: today, attraction_id: row.attraction_id}),
      );
      dispatch(addItemToBag(row));
    }
  }, []);

  const handleTrashPress = useCallback((row, action) => {
    if (action === 'down') {
      dispatch(deleteItemFromBag(row.attraction_id));
    } else if (action === 'up') {
      dispatch(
        deleteDailyPlan({order: today, attraction_id: row.attraction_id}),
      );
    }
  }, []);

  const renderRowUp = useCallback(({data, active}: RenderRowProp) => {
    return (
      <Row
        data={data}
        active={active}
        UporDown="up"
        onPressDown={row => handleRowPress(row, 'down')}
        onPressTrash={row => handleTrashPress(row, 'up')}
      />
    );
  }, []);

  const renderRowDown = useCallback(({data, active}: RenderRowProp) => {
    return (
      <Row
        data={data}
        active={active}
        UporDown="down"
        onPressUp={row => handleRowPress(row, 'up')}
        onPressTrash={row => handleTrashPress(row, 'down')}
      />
    );
  }, []);

  const sortableUpData: {[key: number]: SortableDataItem} = topList.reduce(
    (acc, place, index) => {
      acc[index] = {...place, id: index};
      return acc;
    },
    {} as {[key: number]: SortableDataItem},
  );

  const sortableDownData: {[key: number]: SortableDownDataItem} =
    bottomList.reduce((acc, place, index) => {
      acc[index] = {...place, id: index};
      return acc;
    }, {} as {[key: number]: SortableDownDataItem});

  return (
    <Container>
      <FirstHalf>
        <List
          style={styles.list}
          contentContainerStyle={styles.contentContainer}
          data={sortableUpData}
          renderRow={renderRowUp}
        />
      </FirstHalf>
      <Middle>
        <MiddleTitle>일 예산</MiddleTitle>
        <MiddlePrice>₩123</MiddlePrice>
        <MiddleTitle>총 예산</MiddleTitle>
        <MiddlePrice>₩123</MiddlePrice>
      </Middle>
      <SecondHalf>
        <List
          style={styles.list}
          contentContainerStyle={styles.contentContainer}
          data={sortableDownData}
          renderRow={renderRowDown}
        />
        <DragBar />
      </SecondHalf>
    </Container>
  );
};

const styles = StyleSheet.create({
  list: {
    flex: 1,
  },
  contentContainer: {
    width: window.width,
    ...Platform.select({
      android: {
        paddingHorizontal: 0,
      },
    }),
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: '#fff',
    padding: 8,
    marginHorizontal: 16,
    marginVertical: 9,
    borderRadius: 8,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 3.84,
    elevation: 5,
  },
});

export default PlanDay;
