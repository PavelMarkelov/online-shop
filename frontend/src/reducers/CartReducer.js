import * as actionType from '../actions/ActionsType';
import { images } from "./ProductReducer";


const initialState = {
    cart: []
};

const cartReducer = (state = initialState, action) => {
    switch (action.type) {

        case actionType.USER_CART:
            return {
                ...state,
                cart: action.payload
                    .map(item => Object.assign(item, {image: images.get(item.id)}))
            };

        default:
            return state
    }
};

export default cartReducer;