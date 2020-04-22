import * as actionsType from "./ActionsType";

export const accountEditErrorAction = (error) => {
  return {
    type: actionsType.ACCOUNT_EDIT_FAIL,
    payload: error,
  };
};
