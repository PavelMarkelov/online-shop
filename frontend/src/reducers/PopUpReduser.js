import * as actionsType from "../actions/ActionsType";

const initialState = {
  isVisible: false,
  messageForFail: "",
  messageForSuccess: "",
  messageForLoading: "",
};

const popUpReducer = (state = initialState, action) => {
  switch (action.type) {
    case actionsType.TOGGLE_VISIBILITY_POP_UP:
      return {
        ...state,
        isVisible: action.payload,
      };

    case actionsType.ADD_POP_UP_MESSAGE_FOR_FAIL:
      return {
        ...state,
        messageForFail: action.payload,
        messageForSuccess: "",
        messageForLoading: "",
      };

    case actionsType.ADD_POP_UP_MESSAGE_FOR_SUCCESS:
      return {
        ...state,
        messageForSuccess: action.payload,
        messageForFail: "",
        messageForLoading: "",
      };

    case actionsType.ADD_POP_UP_MESSAGE_FOR_LOADING:
      return {
        ...state,
        messageForLoading: action.payload,
        messageForFail: "",
        messageForSuccess: "",
      };

    default:
      return state;
  }
};

export default popUpReducer;
