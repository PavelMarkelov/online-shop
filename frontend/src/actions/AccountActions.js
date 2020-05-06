import * as actionType from "./ActionsType";
import DataService from "../service/DataService";
import {
  toggleVisibilityAction,
  addPopUpMessageForFailAction,
} from "./PopUpActions";
import * as messages from "../components/templates/pop-up/pop-up-messages";
import { accountEditErrorAction } from "../actions/ErrorActions";
import userRole from "../userRole";

export const loginUserAction = (user) => {
  return {
    type: actionType.LOGIN_USER,
    payload: user,
  };
};

export const logoutUserAction = () => {
  return {
    type: actionType.LOGOUT_USER,
    payload: {},
  };
};

export const userAccountAction = (account) => {
  return {
    type: actionType.USER_ACCOUNT,
    payload: account,
  };
};

export const submitCredentialsAction = (credentials) => {
  return {
    type: actionType.SUBMIT_CREDENTIALS,
    payload: credentials,
  };
};

export const fetchLoginUser = (credentials) => {
  return async (dispatch) => {
    dispatch(submitCredentialsAction(credentials));
    try {
      const response = await DataService.loginRequest(credentials);
      if (response.status === 401) {
        dispatch(addPopUpMessageForFailAction(messages.LOGIN_FAIL));
        dispatch(toggleVisibilityAction(true));
        return;
      }
      const user = await response.json();
      user.role = user.position ? userRole.ADMIN : userRole.CUSTOMER;
      dispatch(loginUserAction(user));
      if (credentials.isRememberMe)
        localStorage.setItem("user", JSON.stringify(user));
    } catch (err) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
    }
  };
};

export const fetchLogout = () => {
  return async (dispatch) => {
    try {
      await DataService.logoutRequest();
      dispatch(logoutUserAction());
      if (localStorage.getItem("user")) localStorage.clear();
      return true;
    } catch (err) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
      return false;
    }
  };
};

export const fetchUserAccount = () => {
  return async (dispatch) => {
    try {
      const response = await DataService.userProfileRequest();
      if (!response.ok) {
        dispatch(addPopUpMessageForFailAction(messages.UNKNOWN_ERROR));
        dispatch(toggleVisibilityAction(true));
        return;
      }
      const user = await response.json();
      user.role = user.position ? userRole.ADMIN : userRole.CUSTOMER;
      dispatch(userAccountAction(user));
    } catch (err) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
    }
  };
};

export const fetchAccountEdit = (account) => {
  return async (dispatch) => {
    try {
      const response = await DataService.editAccountDataRequest(account);
      if (response.status === 403 || response.status === 406) {
        dispatch(accountEditErrorAction(await response.json()));
        return false;
      }
      dispatch(accountEditErrorAction(null));
      dispatch(userAccountAction(await response.json()));
    } catch (err) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
      return false;
    }
    return true;
  };
};
