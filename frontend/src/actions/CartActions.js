import * as actionsType from './ActionsType'


export const userCart = (cart) => {
    return {
        type: actionsType.USER_CART,
        payload: cart
    }
};