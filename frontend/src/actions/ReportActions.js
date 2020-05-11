import * as actionType from "./ActionsType";
import DataService from "../service/DataService";
import {
  addPopUpMessageForFailAction,
  toggleVisibilityAction,
  addPopUpMessageForSuccessAction,
  addPopUpMessageForLoadingAction,
} from "./PopUpActions";
import * as messages from "../components/templates/pop-up/pop-up-messages";

export const loadProductsAction = (products) => {
  return {
    type: actionType.LOAD_PRODUCTS_FOR_REPORT,
    payload: products,
  };
};

export const fetchGetReport = (formValues, isSendToEmail) => {
  return async (dispatch) => {
    if (isSendToEmail) {
      dispatch(addPopUpMessageForLoadingAction(messages.SENDING_IN_PROCESS));
      dispatch(toggleVisibilityAction(true));
    }
    try {
      const response = await DataService.getReportRequest(formValues);
      if (!response.ok) {
        dispatch(addPopUpMessageForFailAction(messages.FAIL_SEND_TO_EMAIL));
        dispatch(toggleVisibilityAction(true));
        return;
      }
      dispatch(loadProductsAction(await response.json()));
      if (isSendToEmail) {
        dispatch(addPopUpMessageForSuccessAction(messages.SUCCESSFULLY_SENT));
        setTimeout(() => dispatch(toggleVisibilityAction(false)), 2500);
      }
    } catch (e) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
    }
  };
};
