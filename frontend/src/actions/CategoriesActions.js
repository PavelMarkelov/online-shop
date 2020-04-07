import * as actionType from './ActionsType'


export const categoriesListAction = (categories) => {
    return {
        type: actionType.CATEGORIES_LIST,
        payload: categories
    }
};