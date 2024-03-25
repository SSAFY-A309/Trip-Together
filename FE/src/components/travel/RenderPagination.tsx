import React from 'react';
import {Modal, FlatList, TouchableWithoutFeedback} from 'react-native';
import {
  PaginationStyle,
  PaginationContainer,
  NavContainer,
  PaginationText,
  DownImage,
  CenteredView,
  ModalView,
  Item,
  ItemText,
  NavButton,
  NavText1,
  NavText2,
} from './RenderPaginationStyle';

interface PaginationProps {
  index: any;
  total: any;
  context: any;
  currentPage: number;
  modalVisible: boolean;
  setCurrentPage: any;
  setModalVisible: any;
  handleMapPress: any;
  handleFinishPress: any;
}

const RenderPagination = ({
  index,
  total,
  context,
  currentPage,
  setCurrentPage,
  modalVisible,
  setModalVisible,
  handleMapPress,
  handleFinishPress,
}: PaginationProps) => {
  const pages = Array.from({length: total}, (_, i) => i + 1);

  return (
    <PaginationStyle>
      <NavContainer>
        <NavButton onPress={handleMapPress}>
          <NavText1>지도</NavText1>
        </NavButton>
      </NavContainer>
      <PaginationContainer onPress={() => setModalVisible(!modalVisible)}>
        <PaginationText>{currentPage + 1} 일차</PaginationText>
        <DownImage source={require('../../assets/images/toggledown.png')} />
      </PaginationContainer>
      <NavContainer>
        <NavButton onPress={handleFinishPress}>
          <NavText2>완료</NavText2>
        </NavButton>
      </NavContainer>
      <Modal
        transparent={true}
        visible={modalVisible}
        onRequestClose={() => setModalVisible(false)}>
        <TouchableWithoutFeedback onPress={() => setModalVisible(false)}>
          <CenteredView>
            <ModalView>
              <FlatList
                data={pages}
                renderItem={({item}) => (
                  <Item
                    onPress={() => {
                      setCurrentPage(item - 1);
                      context.scrollBy(item - index - 1);
                      setModalVisible(false);
                    }}>
                    <ItemText>{item}일차</ItemText>
                  </Item>
                )}
                keyExtractor={item => item.toString()}
              />
            </ModalView>
          </CenteredView>
        </TouchableWithoutFeedback>
      </Modal>
    </PaginationStyle>
  );
};

export default RenderPagination;
