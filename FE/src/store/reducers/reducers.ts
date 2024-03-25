import user from '../slices/user';
import dummy from './../slices/dummy';
import tabState from './../slices/tabState';
import bag from './../slices/bag';
import trip from '../slices/trip';

export interface AppState {
  dummy: ReturnType<typeof dummy>;
  tabState: ReturnType<typeof tabState>;
  user: ReturnType<typeof user>;
  bag: ReturnType<typeof bag>;
  trip: ReturnType<typeof trip>;
}

export const reducers = {
  tabState,
  user,
  bag,
  trip,
};

export const asyncReducers = {
  dummy,
};

export const encryptReducers = {
  dummy,
};
