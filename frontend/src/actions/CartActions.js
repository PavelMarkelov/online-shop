import * as actionsType from "./ActionsType";
import {
  addPopUpMessageForFailAction,
  addPopUpMessageForSuccessAction,
  toggleVisibilityAction,
} from "./PopUpActions";
import DataService from "../service/DataService";
import * as messages from "../components/templates/pop-up/pop-up-messages";

export const addItemsInCartAction = (cart) => {
  return {
    type: actionsType.ADD_ITEMS_IN_CART,
    payload: cart,
  };
};

export const addItemsInCachedCartAction = (cart) => {
  return {
    type: actionsType.ADD_ITEMS_IN_CACHED_CART,
    payload: cart,
  };
};

export const addItemsInCartForEditingAction = (editingCart) => {
  return {
    type: actionsType.ADD_ITEMS_IN_CART_FOR_EDITING,
    payload: editingCart,
  };
};

export const fetchAddItemsInCart = (product) => {
  return async (dispatch) => {
    try {
      const response = await DataService.addToCartRequest(product);
      if (!response.ok) {
        dispatch(addPopUpMessageForFailAction(messages.UNKNOWN_ERROR));
        dispatch(toggleVisibilityAction(true));
        return;
      }
      const cart = await response.json();
      dispatch(addItemsInCartAction(cart));
    } catch (err) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
    }
  };
};

export const fetchUserCart = (cart) => {
  return async (dispatch) => {
    try {
      const response = await DataService.buyCartRequest(cart);
      if (!response.ok) {
        dispatch(addPopUpMessageForFailAction(messages.UNKNOWN_ERROR));
        dispatch(toggleVisibilityAction(true));
        return;
      }
      const remainingCart = await response.json();
      dispatch(addItemsInCartAction(remainingCart.remaining));
      let message = "";
      if (remainingCart.remaining.length) {
        message = remainingCart.bought.length
          ? messages.IF_SOME_ITEMS_ARE_REMAIN
          : messages.IF_ALL_ITEMS_ARE_REMAIN;
      } else message = messages.SUCCESS_CHECKOUT;
      dispatch(addPopUpMessageForSuccessAction(message));
      dispatch(toggleVisibilityAction(true));
    } catch (err) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
    }
  };
};

export const fetchChangeQuantity = (product) => {
  return async (dispatch) => {
    try {
      const response = await DataService.changeCartItemQuantity(product);
      if (!response.ok) {
        dispatch(addPopUpMessageForFailAction(messages.UNKNOWN_ERROR));
        dispatch(toggleVisibilityAction(true));
        return;
      }
      const cart = await response.json();
      dispatch(addItemsInCartAction(cart));
    } catch (err) {
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
    }
  };
};

export const fetchRemoveItem = (id, cartAfterRemoveItem, cachedCart) => {
  return async (dispatch) => {
    dispatch(addItemsInCartForEditingAction(cartAfterRemoveItem));
    try {
      const response = await DataService.removeCartItemRequest(id);
      if (!response.ok) {
        dispatch(addItemsInCartForEditingAction(cachedCart));
        dispatch(addPopUpMessageForFailAction(messages.UNKNOWN_ERROR));
        dispatch(toggleVisibilityAction(true));
        return;
      }
      dispatch(addItemsInCachedCartAction(cartAfterRemoveItem));
    } catch (err) {
      dispatch(addItemsInCartForEditingAction(cachedCart));
      dispatch(addPopUpMessageForFailAction(messages.NETWORK_ERROR));
      dispatch(toggleVisibilityAction(true));
    }
  };
};
