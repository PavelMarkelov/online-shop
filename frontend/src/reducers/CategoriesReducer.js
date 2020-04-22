import * as actionType from "../actions/ActionsType";

const initialState = {
  categoriesList: [],
};

const categoriesReducer = (state = initialState, action) => {
  if (action.type === actionType.CATEGORIES_LIST) {
    return {
      categoriesList: action.payload,
    };
  } else {
    return state;
  }
};

export default categoriesReducer;
