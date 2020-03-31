import * as actionType from './ActionsType'


export const productsList = (products) => {
    return {
        type: actionType.PRODUCTS_LIST,
        payload: products
    }
};

export const productsCategory = products => {
    return {
        type: actionType.PRODUCT_CATEGORY,
        payload: products
    }
};