import React, {useState} from 'react';
import {FlatList} from 'react-native';
import Page from './MainIcons.tsx';
import {Container, Indicator, IndicatorWrapper} from './IconsStyle.tsx';

interface ICarousel {
  gap: number;
  offset: number;
  pages: any[];
  pageWidth: number;
}

const Icons = ({pages, pageWidth, gap, offset}: ICarousel) => {
  const [page, setPage] = useState(0);

  function renderItem({item}: any) {
    return (
      <Page item={item} style={{width: pageWidth, marginHorizontal: gap / 2}} />
    );
  }

  const onScroll = (e: any) => {
    const newPage = Math.round(
      e.nativeEvent.contentOffset.x / (pageWidth + gap),
    );
    setPage(newPage);
  };

  return (
    <Container>
      <FlatList
        automaticallyAdjustContentInsets={false}
        contentContainerStyle={{
          paddingHorizontal: (offset + gap / 2) / 4,
        }}
        data={pages}
        decelerationRate="fast"
        horizontal
        keyExtractor={(item: any) => `page__${item.color}`}
        onScroll={onScroll}
        pagingEnabled
        renderItem={renderItem}
        snapToInterval={pageWidth + gap}
        snapToAlignment="start"
        showsHorizontalScrollIndicator={false}
      />
      <IndicatorWrapper>
        {Array.from({length: pages.length}, (_, i) => i).map(i => (
          <Indicator key={`indicator_${i}`} focused={i === page} />
        ))}
      </IndicatorWrapper>
    </Container>
  );
};

export default Icons;
