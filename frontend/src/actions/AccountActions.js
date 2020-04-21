import * as actionType from './ActionsType';
import DataService from "../service/DataService";
import { toggleVisibilityAction, addPopUpMessageForFailAction } from './PopUpActions';
import * as messages from '../components/templates/pop-up/pop-up-messages';

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

export const submitCredentialsAction = credentials => {
    return {
        type: actionType.SUBMIT_CREDENTIALS,
        payload: credentials
    }
};

export const fetchLoginUser = credentials => {
    return async dispatch => {
        dispatch(submitCredentialsAction(credentials));
        try {
            const response = await DataService.loginRequest(credentials);
            if (response.status === 401) {
                dispatch(addPopUpMessageForFailAction(messages.LOGIN_FAIL));
                dispatch(toggleVisibilityAction(true));
                return
            }
            const user = await response.json();
            dispatch(loginUserAction(user));
        } catch (err) {
            dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
            dispatch(toggleVisibilityAction(true));
        }
    }
};