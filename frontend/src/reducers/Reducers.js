import { combineReducers } from 'redux';
import * as actions from '../actions/ActionsType';

const loginReducer = (state = {}, action) => {
    if (action.type === actions.LOGIN_USER)
        return {
            ...state,
            user: action.payload
        };
    return state;
};

const reducers = combineReducers({
    loginReducer
});

export default reducers;