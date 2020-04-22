import * as actionType from "./ActionsType";

export const productsListAction = (products) => {
  return {
    type: actionType.PRODUCTS_LIST,
    payload: products,
  };
};

export const productsFromCategoryAction = (products) => {
  return {
    type: actionType.PRODUCT_FROM_SELECTED_CATEGORY,
    payload: products,
  };
};

export const enableFilterAction = (filter) => {
  return {
    type: actionType.ENABLE_FILTER,
    payload: filter,
  };
};

export const disableFilterAction = (products) => {
  return {
    type: actionType.DISABLE_FILTER,
    payload: products,
  };
};

export const productDetailsAction = (product) => {
  return {
    type: actionType.PRODUCT_DETAILS,
    payload: product,
  };
};
