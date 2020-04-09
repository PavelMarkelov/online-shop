import * as actionsType from './ActionsType'

export const toggleVisibility = isVisible => {
    return {
        type: actionsType.TOGGLE_VISIBILITY_POP_UP,
        payload: isVisible
    }
};

export const addPopUpMessage = message => {
    return {
        type: actionsType.ADD_POP_UP_MESSAGE,
        payload: message
    }
};