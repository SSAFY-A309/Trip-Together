import React from 'react';
import {FlatList} from 'react-native';
import Page from './Page';
import {Container, PackageTitle} from './PackageStyle';

interface ICarousel {
  gap: number;
  offset: number;
  pages: any[];
  pageWidth: number;
}

const Package = ({pages, pageWidth, gap, offset}: ICarousel) => {
  const renderItem = ({item}: any) => {
    return (
      <Page item={item} style={{width: pageWidth, marginHorizontal: gap / 2}} />
    );
  };

  return (
    <Container>
      <PackageTitle>여행 상품 추천</PackageTitle>
      <FlatList
        automaticallyAdjustContentInsets={false}
        contentContainerStyle={{
          paddingHorizontal: (offset + gap / 2) / 4,
        }}
        data={pages}
        decelerationRate="fast"
        horizontal
        keyExtractor={(item: any) => `page__${item.color}`}
        pagingEnabled
        renderItem={renderItem}
        snapToInterval={pageWidth + gap}
        snapToAlignment="start"
        showsHorizontalScrollIndicator={false}
      />
    </Container>
  );
};

export default Package;
