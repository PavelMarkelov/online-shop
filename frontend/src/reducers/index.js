import { combineReducers } from "redux";

import userReducer from "./UserReducer";
import categoriesReducer from "./CategoriesReducer";
import productReducer from "./ProductReducer";
import cartReducer from "./CartReducer";
import errorReducer from "./ErrorReducer";
import popUpReducer from "./PopUpReduser";
import { reducer as reduxFormReducer } from "redux-form";

const reducers = combineReducers({
  userState: userReducer,
  categoriesState: categoriesReducer,
  productState: productReducer,
  cartState: cartReducer,
  errorState: errorReducer,
  popUpState: popUpReducer,
  form: reduxFormReducer,
});

export default reducers;
