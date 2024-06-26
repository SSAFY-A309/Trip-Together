import {createNativeStackNavigator} from '@react-navigation/native-stack';
import React from 'react';
import ToChat from '../components/flashMob/ChatButton';
import {FlashMobStackParams} from '../interfaces/router/flashMob/FlashMobStackParams';
import PlaceDetail from '../pages/common/PlaceDetail';
import PlaceInfoList from '../pages/common/PlaceInfoList';
import FlashCreate from '../pages/flashMob/FlashCreate';
import FlashList from '../pages/flashMob/FlashList';
import FlashMain from '../pages/flashMob/FlashMain';
import ChatMain from '../pages/flashMob/chat/ChatMain';
import ChatRoom from '../pages/flashMob/chat/ChatRoom';
import SelectHistory from '../pages/flashMob/settlement/SelectHistory';
import SelectPeople from '../pages/flashMob/settlement/SelectPeople';
import Settlement from '../pages/flashMob/settlement/Settlement';
import SettlementConfirm from '../pages/flashMob/settlement/SettlementConfirm';
import CurSitu from '../pages/flashMob/tranHistory/CurSitu';
import Receipt from '../pages/flashMob/tranHistory/Receipt';
import TranHistory from '../pages/flashMob/tranHistory/TranHistory';

const FlashMobStack = createNativeStackNavigator<FlashMobStackParams>();

const FlashMobNavigator = () => {
  return (
    <FlashMobStack.Navigator>
      <FlashMobStack.Screen
        name="FlashMain"
        component={FlashMain}
        options={{title: '번개 모임', headerRight: ToChat}}
      />
      <FlashMobStack.Group screenOptions={{title: '', headerRight: ToChat}}>
        <FlashMobStack.Screen
          name="FlashPlaces"
          component={PlaceInfoList}
          options={{
            title: '',
            fullScreenGestureEnabled: true,
            customAnimationOnGesture: true,
            animation: 'slide_from_bottom',
          }}
        />
        <FlashMobStack.Screen name="FlashPlace" component={PlaceDetail} />
        <FlashMobStack.Screen name="FlashCreate" component={FlashCreate} />
        <FlashMobStack.Screen name="FlashList" component={FlashList} />
      </FlashMobStack.Group>

      <FlashMobStack.Group
        screenOptions={{title: '채팅', headerTitleAlign: 'center'}}>
        <FlashMobStack.Screen name="ChatMain" component={ChatMain} />
        <FlashMobStack.Screen name="ChatRoom" component={ChatRoom} />
      </FlashMobStack.Group>
      <FlashMobStack.Group
        screenOptions={{title: '정산하기', headerTitleAlign: 'center'}}>
        <FlashMobStack.Screen name="Settlement" component={Settlement} />
        <FlashMobStack.Screen name="SelectHistory" component={SelectHistory} />
        <FlashMobStack.Screen name="SelectPeople" component={SelectPeople} />
        <FlashMobStack.Screen
          name="SettlementConfirm"
          component={SettlementConfirm}
        />
      </FlashMobStack.Group>
      <FlashMobStack.Group screenOptions={{headerTitleAlign: 'center'}}>
        <FlashMobStack.Screen
          name="TranHistory"
          component={TranHistory}
          options={{title: '정산내역'}}
        />
        <FlashMobStack.Screen
          name="Receipt"
          component={Receipt}
          options={{title: '영수증 보기'}}
        />
        <FlashMobStack.Screen
          name="CurSitu"
          component={CurSitu}
          options={{title: '정산현황'}}
        />
      </FlashMobStack.Group>
    </FlashMobStack.Navigator>
  );
};

export default FlashMobNavigator;
