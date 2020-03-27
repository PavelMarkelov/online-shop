import { combineReducers } from 'redux';

import userReducer from "./UserReducer";


const reducers = combineReducers({
    userState: userReducer,
});

export default reducers;