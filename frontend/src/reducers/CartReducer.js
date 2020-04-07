import * as actionType from '../actions/ActionsType';
import { images } from "./ProductReducer";


const initialState = {
    cart: []
};

const cartReducer = (state = initialState, action) => {
    switch (action.type) {

        case actionType.ADD_ITEMS_IN_USER_CART:
            return {
                ...state,
                cart: action.payload
                    .map(item => Object.assign(item, {image: images.get(item.id)}))
            };

        case actionType.REMOVE_CART_ITEM:
            return {
                ...state,
                cart: state.cart.filter(value => value.id !== action.payload)
            };

        default:
            return state
    }
};

export default cartReducer;