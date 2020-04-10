import * as actionType from '../actions/ActionsType';

const initialState = {
    isLoginError: false,
    error: null
};

const errorReducer = (state = initialState, action) => {
    if (actionType.ACCOUNT_EDIT_FAIL)
        return {
            ...state,
            error: action.payload
        };
    return state;
};

export default errorReducer;