import * as actionsType from './ActionsType'


export const userCart = (cart) => {
    return {
        type: actionsType.ADD_ITEMS_IN_USER_CART,
        payload: cart
    }
};

export const removeCartItem = (id) => {
    return {
        type: actionsType.REMOVE_CART_ITEM,
        payload: id
    }
};