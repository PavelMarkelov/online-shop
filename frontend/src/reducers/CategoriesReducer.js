import * as actionType from '../actions/ActionsType';


const initialState = {
    categoriesList: []
};

const categoriesReducer = (state = initialState, action) => {
    switch (action.type) {

        case actionType.CATEGORIES_LIST:
            return {
                categoriesList: action.payload
            };

        default:
            return state
    }
};

export default categoriesReducer;