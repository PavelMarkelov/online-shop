class DataService {
  static _REST_API_URL = "http://localhost:8080/api";
  static _REQUEST_PARAMS_FOR_GET = {
    credentials: "include",
  };

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
      this._REST_API_URL + "/sessions",
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
    return fetch(this._REST_API_URL + "/sessions", params);
  }

  static userProfileRequest() {
    return fetch(
      this._REST_API_URL + "/accounts",
      this._REQUEST_PARAMS_FOR_GET
    );
  }

  static editAccountDataRequest(newAccountData) {
    return fetch(
      this._REST_API_URL + "/clients",
      this._postOrPutRequestParams("put", newAccountData)
    );
  }

  static categoriesListRequest() {
    return fetch(
      this._REST_API_URL + "/categories",
      this._REQUEST_PARAMS_FOR_GET
    );
  }

  static productsListRequest() {
    return fetch(
      this._REST_API_URL + "/products",
      this._REQUEST_PARAMS_FOR_GET
    );
  }

  static productsCategoryRequest(categoryId) {
    return fetch(
      this._REST_API_URL + `/products?category=${categoryId}`,
      this._REQUEST_PARAMS_FOR_GET
    );
  }

  static productRequest(id) {
    return fetch(
      this._REST_API_URL + `/products/${id}`,
      this._REQUEST_PARAMS_FOR_GET
    );
  }

  static addToCartRequest(product) {
    return fetch(
      this._REST_API_URL + "/baskets",
      this._postOrPutRequestParams("post", product)
    );
  }

  static cartRequest() {
    return fetch(this._REST_API_URL + "/baskets", this._REQUEST_PARAMS_FOR_GET);
  }

  static removeCartItemRequest(id) {
    const params = {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      credentials: "include",
    };
    return fetch(this._REST_API_URL + `/baskets/${id}`, params);
  }

  static changeCartItemQuantity(product) {
    return fetch(
      this._REST_API_URL + "/baskets",
      this._postOrPutRequestParams("put", product)
    );
  }

  static buyCartRequest(cart) {
    return fetch(
      this._REST_API_URL + "/purchases/baskets",
      this._postOrPutRequestParams("post", cart)
    );
  }

  static getReportRequest(formValues) {
    return fetch(
      this._REST_API_URL + "/products/report",
      this._postOrPutRequestParams("post", formValues)
    );
  }

  static downloadReport(minCount, maxCount) {
    return fetch(
      this._REST_API_URL +
        `/products/report/download?minCount=${minCount}&maxCount=${maxCount}`,
      this._REQUEST_PARAMS_FOR_GET
    );
  }
}

export default DataService;
