import React from 'react';
import {imagePath} from '../../assets/images/imagePath';
import {
  DdayText,
  DurationText,
  ButtonImage,
  MoneyImage,
  MoneyText,
  MoneyUnit,
  PlanCenter,
  PlanDescription,
  PlanDescriptionBox,
  PlanImage,
  PlanMoney,
  PlanSlideButton,
  PlanTitle,
  PlanView,
  CityName,
} from './PlansStyle';

const Plans = () => {
  return (
    <PlanView>
      <PlanTitle>오희주님의 프랑스 여행 계획</PlanTitle>
      <PlanCenter>
        <PlanSlideButton>
          <ButtonImage source={require('../../assets/images/left-arrow.png')} />
        </PlanSlideButton>
        <PlanDescription>
          <PlanDescriptionBox>
            <PlanImage source={imagePath.France} />
            <CityName>몽펠리에</CityName>
          </PlanDescriptionBox>
          <PlanDescriptionBox>
            <DdayText>D-52</DdayText>
            <DurationText>총 12일</DurationText>
          </PlanDescriptionBox>
        </PlanDescription>
        <PlanSlideButton>
          <ButtonImage
            source={require('../../assets/images/right-arrow.png')}
          />
        </PlanSlideButton>
      </PlanCenter>
      <PlanMoney>
        <MoneyImage source={require('../../assets/images/profits.png')} />
        <MoneyUnit>₩</MoneyUnit>
        <MoneyText>3,200,000</MoneyText>
      </PlanMoney>
    </PlanView>
  );
};

export default Plans;
