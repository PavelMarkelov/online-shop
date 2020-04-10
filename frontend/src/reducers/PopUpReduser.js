import * as actionsType from '../actions/ActionsType'

const initialState = {
    isVisible: false,
    message: null
};

const popUpReducer = (state = initialState, action) => {
    switch (action.type) {
        case actionsType.TOGGLE_VISIBILITY_POP_UP:
            return {
                ...state,
                isVisible: action.payload
            };
        case actionsType.ADD_POP_UP_MESSAGE:
            return {
                ...state,
                message: action.payload
            };
        default:
            return state
    }
};

export default popUpReducer;