import { combineReducers } from 'redux';

import userReducer from "./UserReducer";
import categoriesReducer from "./CategoriesReducer";
import productReducer from "./ProductReducer";


const reducers = combineReducers({
    userState: userReducer,
    categoriesState: categoriesReducer,
    productState: productReducer,
});

export default reducers;