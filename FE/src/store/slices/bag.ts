import {PayloadAction, createSlice} from '@reduxjs/toolkit';
import {BagState, BagItem} from '../../interfaces/states/BagState';

const initialState: BagState = {
  bagInfo: [
    {
      attraction_id: 11,
      thumbnail_image_url: '',
      name: 'La Sagrada Familia',
      address: '',
      avg_rating: 4.4,
      avg_price: 123,
    },
    {
      attraction_id: 12,
      thumbnail_image_url: '',
      name: 'La Sagrada Familia',
      address: '',
      avg_rating: 3.4,
      avg_price: 123,
    },
    {
      attraction_id: 13,
      thumbnail_image_url: '',
      name: 'La Sagrada Familia',
      address: '',
      avg_rating: 2.4,
      avg_price: 123,
    },
  ],
};

export const bagSlice = createSlice({
  name: 'bag',
  initialState,
  reducers: {
    addItemToBag: (state, action: PayloadAction<BagItem>) => {
      state.bagInfo.push(action.payload);
    },
    deleteItemFromBag: (state, action: PayloadAction<number>) => {
      state.bagInfo = state.bagInfo.filter(
        item => item.attraction_id !== action.payload,
      );
    },
  },
});

export const {addItemToBag, deleteItemFromBag} = bagSlice.actions;

export default bagSlice.reducer;
