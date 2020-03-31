import * as actionType from './ActionsType'


export const categoriesList = (categories) => {
    return {
        type: actionType.CATEGORIES_LIST,
        payload: categories
    }
};