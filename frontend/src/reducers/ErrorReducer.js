import * as actionType from '../actions/ActionsType';

const initialState = {
    error: null
};

const errorReducer = (state = initialState, action) => {
    if (action.type === actionType.ACCOUNT_EDIT_FAIL)
        return {
            ...state,
            error: action.payload
        };
    return state;
};

export default errorReducer;