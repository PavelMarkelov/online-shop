import { combineReducers } from "redux";

import userReducer from "./UserReducer";
import categoriesReducer from "./CategoriesReducer";
import productReducer from "./ProductReducer";
import cartReducer from "./CartReducer";
import errorReducer from "./ErrorReducer";
import popUpReducer from "./PopUpReduser";

const reducers = combineReducers({
  userState: userReducer,
  categoriesState: categoriesReducer,
  productState: productReducer,
  cartState: cartReducer,
  errorState: errorReducer,
  popUpState: popUpReducer,
});

export default reducers;
