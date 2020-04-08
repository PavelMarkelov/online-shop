import * as actionsType from './ActionsType';


export const loginErrorAction = (isLoginFalse) => {
    return {
        type: actionsType.LOGIN_FAIL,
        payload: isLoginFalse
    }
};

export const accountEditErrorAction = (error) => {
    return {
        type: actionsType.ACCOUNT_EDIT_FAIL,
        payload: error
    }
};