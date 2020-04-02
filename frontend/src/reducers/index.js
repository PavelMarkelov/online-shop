import { combineReducers } from 'redux';

import userReducer from "./UserReducer";
import categoriesReducer from "./CategoriesReducer";
import productReducer from "./ProductReducer";
import cartReducer from './CartReducer';


const reducers = combineReducers({
    userState: userReducer,
    categoriesState: categoriesReducer,
    productState: productReducer,
    cartState: cartReducer
});

export default reducers;