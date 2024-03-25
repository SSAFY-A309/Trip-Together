import {PayloadAction, createSlice} from '@reduxjs/toolkit';
import {TripState} from '../../interfaces/states/TripState';

interface attractionProp {
  attraction_id: number;
  thumbnail_image_url: string;
  name: string;
  address: string;
  avg_rating: string;
  avg_price: string;
}

interface dailyplanProp {
  order: number;
  attraction: attractionProp;
}

interface dailydeleteProp {
  order: number;
  attraction_id: number;
}

const initialState: TripState = {
  tripInfo: {
    plan_id: 0,
    start_region: '몽펠리에',
    start_at: '',
    end_at: '',
    title: '',
    total_estimated_budget: 0,
    total_budget: 0,
    status: 'done',
    daily_plans: [],
  },
};

export const tripSlice = createSlice({
  name: 'trip',
  initialState,
  reducers: {
    setStartRegion: (state, action: PayloadAction<string>) => {
      state.tripInfo.start_region = action.payload;
    },
    setDate: (
      state,
      action: PayloadAction<{start_at: string; end_at: string}>,
    ) => {
      state.tripInfo.start_at = action.payload.start_at;
      state.tripInfo.end_at = action.payload.end_at;

      const startDate = new Date(action.payload.start_at);
      const endDate = new Date(action.payload.end_at);
      const diffTime = Math.abs(endDate.getTime() - startDate.getTime());
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;

      state.tripInfo.daily_plans = Array.from(
        {length: diffDays},
        (_, index) => {
          return {
            attractions: [],
            order: index,
            daily_estimated_budget: 0,
          };
        },
      );
    },
    setTripTitle: (state, action: PayloadAction<string>) => {
      state.tripInfo.title = action.payload;
    },
    addDailyPlan: (state, action: PayloadAction<dailyplanProp>) => {
      const {order, attraction} = action.payload;
      state.tripInfo.daily_plans[order].attractions.push(attraction);
    },
    deleteDailyPlan: (state, action: PayloadAction<dailydeleteProp>) => {
      const {order, attraction_id} = action.payload;
      state.tripInfo.daily_plans[order].attractions =
        state.tripInfo.daily_plans[order].attractions.filter(
          item => item.attraction_id !== attraction_id,
        );
    },
  },
});

export const {
  setStartRegion,
  setDate,
  setTripTitle,
  addDailyPlan,
  deleteDailyPlan,
} = tripSlice.actions;

export default tripSlice.reducer;
