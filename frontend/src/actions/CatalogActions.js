import DataService from "../service/DataService";
import {
  toggleVisibilityAction,
  addPopUpMessageForFailAction,
} from "./PopUpActions";
import * as messages from "../components/templates/pop-up/pop-up-messages";
import { categoriesListAction } from "./CategoriesActions";
import { productsListAction } from "./ProductActions";
import { addItemsInCartAction } from "./CartActions";

const loadDataWithCart = () => {
  return async (dispatch) => {
    try {
      const responses = await Promise.all([
        DataService.productsListRequest(),
        DataService.categoriesListRequest(),
        DataService.cartRequest(),
      ]);
      if (!responses[0].ok || !responses[1].ok || !responses[2].ok) {
        dispatch(addPopUpMessageForFailAction(messages.UNKNOWN_ERROR));
        dispatch(toggleVisibilityAction(true));
        return;
      }
      const data = await Promise.all(
        responses.map((response) => response.json())
      );
      dispatch(productsListAction(data[0]));
      dispatch(categoriesListAction(data[1]));
      dispatch(addItemsInCartAction(data[2]));
    } catch (err) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
    }
  };
};

const loadDataWithoutCart = () => {
  return async (dispatch) => {
    try {
      const responses = await Promise.all([
        DataService.productsListRequest(),
        DataService.categoriesListRequest(),
      ]);
      if (!responses[0].ok || !responses[1].ok) {
        dispatch(addPopUpMessageForFailAction(messages.UNKNOWN_ERROR));
        dispatch(toggleVisibilityAction(true));
        return;
      }
      const data = await Promise.all(
        responses.map((response) => response.json())
      );
      dispatch(productsListAction(data[0]));
      dispatch(categoriesListAction(data[1]));
    } catch (err) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
    }
  };
};

export const loadDataAction = (isCartIsLoaded) => {
  return async (dispatch) => {
    isCartIsLoaded
      ? dispatch(loadDataWithoutCart())
      : dispatch(loadDataWithCart());
  };
};
