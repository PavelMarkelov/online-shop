import * as actionType from "../actions/ActionsType";

const initialState = {
  formData: {
    minCount: 0,
    maxCount: 0,
    email: "",
    isOutOfStock: false,
    isSentToEmail: false,
  },
  products: [],
  isLoading: false,
};

const reportFormReducer = (state = initialState, action) => {
  switch (action.type) {
    case actionType.LOAD_PRODUCTS_FOR_REPORT:
      return {
        ...state,
        products: action.payload,
      };
    case actionType.LOADING_REPORT_FOR_DOWNLOAD:
      return {
        ...state,
        isLoading: action.payload,
      };
    default:
      return state;
  }
};

export default reportFormReducer;
