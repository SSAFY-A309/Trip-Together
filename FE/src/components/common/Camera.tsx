import React, {useState} from 'react';
import {TouchableOpacity, Modal} from 'react-native';
import styled from 'styled-components/native';
import QRScanner from './QRscanner';

const Wrapper = styled(TouchableOpacity)`
  width: 30px;
  height: 30px;
`;

const Image = styled.Image`
  width: 30px;
  height: 30px;
`;

const Camera: React.FC = () => {
  const [modalVisible, setModalVisible] = useState(false);

  const onPress = () => {
    setModalVisible(true);
  };

  return (
    <>
      <Wrapper onPress={onPress}>
        <Image source={require('../../assets/images/qrcode.png')} />
      </Wrapper>
      <Modal
        animationType="slide"
        transparent={true}
        visible={modalVisible}
        onRequestClose={() => {
          setModalVisible(false);
        }}>
        <QRScanner />
      </Modal>
    </>
  );
};

export default Camera;
