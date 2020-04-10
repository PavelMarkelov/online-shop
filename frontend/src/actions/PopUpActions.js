import * as actionsType from './ActionsType'

export const toggleVisibilityAction = isVisible => {
    return {
        type: actionsType.TOGGLE_VISIBILITY_POP_UP,
        payload: isVisible
    }
};

export const addPopUpMessageAction = message => {
    return {
        type: actionsType.ADD_POP_UP_MESSAGE,
        payload: message
    }
};