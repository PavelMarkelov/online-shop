import * as actionType from "../actions/ActionsType";
import { images } from "./ProductReducer";

const initialState = {
  isCartIsLoaded: false,
  cachedCart: [],
  editingCart: [],
};

const cartReducer = (state = initialState, action) => {
  switch (action.type) {
    case actionType.ADD_ITEMS_IN_CART:
      const payload = action.payload.map((item) =>
        Object.assign(item, { image: images.get(item.id) })
      );
      return {
        isCartIsLoaded: true,
        cachedCart: payload.slice(),
        editingCart: payload.slice(),
      };

    case actionType.ADD_ITEMS_IN_CACHED_CART:
      return {
        ...state,
        cachedCart: action.payload.map((item) =>
          Object.assign(item, { image: images.get(item.id) })
        ),
      };

    case actionType.ADD_ITEMS_IN_CART_FOR_EDITING:
      return {
        ...state,
        editingCart: action.payload.map((item) =>
          Object.assign(item, { image: images.get(item.id) })
        ),
      };

    default:
      return state;
  }
};

export default cartReducer;
