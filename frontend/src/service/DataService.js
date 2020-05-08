const REST_API_URL = "http://localhost:8080/api";
const getRequestParams = {
  credentials: "include",
};

class DataService {
  static _postOrPutRequestParams(method, bodyObj) {
    return {
      method: method.toUpperCase(),
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify(bodyObj),
      credentials: "include",
    };
  }

  static loginRequest(credentials) {
    return fetch(
      REST_API_URL + "/sessions",
      this._postOrPutRequestParams("post", credentials)
    );
  }

  static logoutRequest() {
    const params = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      credentials: "include",
    };
    return fetch(REST_API_URL + "/sessions", params);
  }

  static userProfileRequest() {
    return fetch(REST_API_URL + "/accounts", getRequestParams);
  }

  static editAccountDataRequest(newAccountData) {
    return fetch(
      REST_API_URL + "/clients",
      this._postOrPutRequestParams("put", newAccountData)
    );
  }

  static categoriesListRequest() {
    return fetch(REST_API_URL + "/categories", getRequestParams);
  }

  static productsListRequest() {
    return fetch(REST_API_URL + "/products", getRequestParams);
  }

  static productsCategoryRequest(categoryId) {
    return fetch(
      REST_API_URL + `/products?category=${categoryId}`,
      getRequestParams
    );
  }

  static productRequest(id) {
    return fetch(REST_API_URL + `/products/${id}`, getRequestParams);
  }

  static addToCartRequest(product) {
    return fetch(
      REST_API_URL + "/baskets",
      this._postOrPutRequestParams("post", product)
    );
  }

  static cartRequest() {
    return fetch(REST_API_URL + "/baskets", getRequestParams);
  }

  static removeCartItemRequest(id) {
    const params = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      credentials: "include",
    };
    return fetch(REST_API_URL + `/baskets/${id}`, params);
  }

  static changeCartItemQuantity(product) {
    return fetch(
      REST_API_URL + "/baskets",
      this._postOrPutRequestParams("put", product)
    );
  }

  static buyCartRequest(cart) {
    return fetch(
      REST_API_URL + "/purchases/baskets",
      this._postOrPutRequestParams("post", cart)
    );
  }

  static getReportRequest(formValues) {
    return fetch(
      REST_API_URL + "/products/report",
      this._postOrPutRequestParams("post", formValues)
    );
  }
}

export default DataService;
