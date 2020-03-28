import * as actionType from './ActionsType';

export const loginUser = (user) => {
    return {
        type: actionType.LOGIN_USER,
        payload: user
    }
};

export const logoutUser = () => {
    return {
        type: actionType.LOGOUT_USER,
        payload: {}
    }
};

export const userAccount = (account) => {
    return {
        type: actionType.USER_ACCOUNT,
        payload: account
    }
};