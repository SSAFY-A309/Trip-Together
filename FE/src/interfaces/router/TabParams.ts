import {ParamListBase} from '@react-navigation/native';
import {MyPageStackParams} from './MyPageStackParams';

interface TabParams extends ParamListBase, MyPageStackParams {
  Trip: undefined;
  FlashMob: undefined;
  MyPage: undefined;
}

export type {TabParams};
