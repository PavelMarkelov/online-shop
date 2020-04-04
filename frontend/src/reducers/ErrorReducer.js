import * as actionType from '../actions/ActionsType';

const initialState = {
    isLoginError: false,
    error: null
};

const errorReducer = (state = initialState, action) => {
    switch (action.type) {
        case actionType.LOGIN_FAIL:
            return {
                ...state,
                isLoginFail: action.payload
            };
        
        case actionType.ACCOUNT_EDIT_FAIL:
            return {
                ...state,
                error: action.payload
            };
            
        default:
            return state;
    } 
};

export default errorReducer;