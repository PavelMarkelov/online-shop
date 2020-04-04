import * as actionsType from './ActionsType';


export const loginError = (isLoginFalse) => {
    return {
        type: actionsType.LOGIN_FAIL,
        payload: isLoginFalse
    }
};

export const accountEditError = (error) => {
    return {
        type: actionsType.ACCOUNT_EDIT_FAIL,
        payload: error
    }
};