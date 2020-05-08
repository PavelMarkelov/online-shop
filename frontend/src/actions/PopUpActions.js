import * as actionsType from "./ActionsType";

export const toggleVisibilityAction = (isVisible) => {
  return {
    type: actionsType.TOGGLE_VISIBILITY_POP_UP,
    payload: isVisible,
  };
};

export const addPopUpMessageForFailAction = (message) => {
  return {
    type: actionsType.ADD_POP_UP_MESSAGE_FOR_FAIL,
    payload: message,
  };
};

export const addPopUpMessageForSuccessAction = (message) => {
  return {
    type: actionsType.ADD_POP_UP_MESSAGE_FOR_SUCCESS,
    payload: message,
  };
};

export const addPopUpMessageForLoadingAction = (message) => {
  return {
    type: actionsType.ADD_POP_UP_MESSAGE_FOR_LOADING,
    payload: message,
  };
};
