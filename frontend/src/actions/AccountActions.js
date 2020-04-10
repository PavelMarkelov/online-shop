import * as actionType from './ActionsType';
import DataService from "../service/DataService";
import { toggleVisibilityAction, addPopUpMessageAction } from './PopUpActions';

export const loginUserAction = (user) => {
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

export const userAccountAction = (account) => {
    return {
        type: actionType.USER_ACCOUNT,
        payload: account
    }
};

export const submitCredentials = credentials => {
    return {
        type: actionType.SUBMIT_CREDENTIALS,
        payload: credentials
    }
};

export const fetchLoginUser = credentials => {
    return async dispatch => {
        dispatch(submitCredentials(credentials));
        try {
            const response = await DataService.loginRequest(credentials);
            if (response.status === 401) {
                dispatch(addPopUpMessageAction('Invalid login or password!'));
                dispatch(toggleVisibilityAction(true));
                return
            }
            const user = await response.json();
            dispatch(loginUserAction(user));
        } catch (err) {
            dispatch(addPopUpMessageAction());
            dispatch(toggleVisibilityAction(true));
        }
    }
};