import {PayloadAction, createSlice} from '@reduxjs/toolkit';
import {UserState} from '../../interfaces/states/UserState';

const initialState: UserState = {
  isLoggedIn: false,
  userInfo: {
    user_id: 0,
    username: '김태규',
    nickname: '',
    image_url: '',
    description: '',
    is_pin: false,
    trip_accounts: [
      {id: 0, nation: 'UK', nation_kr: '영국', unit: 8356, balance: 28.88},
      {id: 1, nation: 'EU', nation_kr: 'EU', unit: 8364, balance: 485.88},
    ],
    trip_accounts_length: 2,
  },
};

export const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    login: (state, action: PayloadAction<boolean>) => {
      state.isLoggedIn = action.payload;
    },
  },
});

export const {login} = userSlice.actions;

export default userSlice.reducer;
