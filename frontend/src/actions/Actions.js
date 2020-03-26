import * as type from './ActionsType';

export const loginUser = (user) => {
    return {
        type: type.LOGIN_USER,
        payload: user
    };
};