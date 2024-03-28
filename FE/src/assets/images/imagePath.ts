import {ImageSourcePropType} from 'react-native';

interface ImagePath {
  [key: string | number]: ImageSourcePropType | undefined;
  europe: ImageSourcePropType;
  europe2: ImageSourcePropType;
  europe3: ImageSourcePropType;
  europe4: ImageSourcePropType;
  europe5: ImageSourcePropType;
  adh01: ImageSourcePropType;
  bag: ImageSourcePropType;
  bankLogo: ImageSourcePropType;
  basicProfile: ImageSourcePropType;
  camera: ImageSourcePropType;
  CurExchange: ImageSourcePropType;
  down: ImageSourcePropType;
  drag: ImageSourcePropType;
  EU: ImageSourcePropType;
  exchange: ImageSourcePropType;
  France: ImageSourcePropType;
  godown: ImageSourcePropType;
  goup: ImageSourcePropType;
  insurance: ImageSourcePropType;
  KR: ImageSourcePropType;
  leftArrow: ImageSourcePropType;
  lightning: ImageSourcePropType;
  logo: ImageSourcePropType;
  money: ImageSourcePropType;
  myPage: ImageSourcePropType;
  pay: ImageSourcePropType;
  plane: ImageSourcePropType;
  planning: ImageSourcePropType;
  profits: ImageSourcePropType;
  qrcode: ImageSourcePropType;
  remit: ImageSourcePropType;
  review: ImageSourcePropType;
  rightArrow: ImageSourcePropType;
  sagradafamilla: ImageSourcePropType;
  shopping: ImageSourcePropType;
  sync: ImageSourcePropType;
  toggledown: ImageSourcePropType;
  trash: ImageSourcePropType;
  trip: ImageSourcePropType;
  UK: ImageSourcePropType;
  up: ImageSourcePropType;
}

export const imagePath: ImagePath = {
  europe: require('./commercials/europe.jpg'),
  europe2: require('./commercials/europe2.jpg'),
  europe3: require('./commercials/europe3.jpg'),
  europe4: require('./commercials/europe4.jpg'),
  europe5: require('./commercials/europe5.jpg'),
  adh01: require('./adh01.png'),
  bag: require('./bag.png'),
  bankLogo: require('./bankLogo.png'),
  basicProfile: require('./basicProfile.png'),
  camera: require('./camera.png'),
  CurExchange: require('./CurExchange.png'),
  down: require('./down.png'),
  drag: require('./drag.png'),
  EU: require('./EU_round.png'),
  exchange: require('./exchange.png'),
  France: require('./France.png'),
  godown: require('./godown.png'),
  goup: require('./goup.png'),
  insurance: require('./insurance.png'),
  KR: require('./KR_round.png'),
  leftArrow: require('./left-arrow.png'),
  lightning: require('./lightning.png'),
  logo: require('./logo.png'),
  money: require('./money.png'),
  myPage: require('./myPage.png'),
  pay: require('./pay.png'),
  plane: require('./plane.png'),
  planning: require('./planning.png'),
  profits: require('./profits.png'),
  qrcode: require('./qrcode.png'),
  remit: require('./remit.png'),
  review: require('./review.jpg'),
  rightArrow: require('./right-arrow.png'),
  sagradafamilla: require('./sagradafamilia.png'),
  shopping: require('./shopping.png'),
  sync: require('./sync.png'),
  toggledown: require('./toggledown.png'),
  trash: require('./trash.png'),
  trip: require('./trip.png'),
  UK: require('./UK_round.png'),
  up: require('./up.png'),
};
