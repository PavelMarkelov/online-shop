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

export const fetchLoginUser = (credentials, callback) => {
    return async dispatch => {
        try {
            const response = await DataService.loginRequest(credentials);
            if (response.status === 401) {
                dispatch(addPopUpMessageAction('Invalid login or password!'));
                dispatch(toggleVisibilityAction(true));
                return
            }
            const user = await response.json();
            dispatch(loginUserAction(user));
//            Нужно как-то переадресовать
//           history.push('/catalog')
        } catch (err) {
            dispatch(toggleVisibilityAction(true));
        }
    }
};