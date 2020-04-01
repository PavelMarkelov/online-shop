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

export const enableFilter = filter => {
    return {
        type: actionType.ENABLE_FILTER,
        payload: filter
    }
};

export const disableFilter = products => {
    return {
        type: actionType.DISABLE_FILTER,
        payload: products
    }
};

export const productDescription = product => {
    return {
        type: actionType.PRODUCT_DESCRIPTION,
        payload: product
    }
};