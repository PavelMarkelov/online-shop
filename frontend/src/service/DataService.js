const REST_API_URL = "http://localhost:8080/api";
const getRequestParams = {
  credentials: "include",
};

class DataService {
  static loginRequest(credentials) {
    const params = {
      method: "POST",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify(credentials),
      credentials: "include",
    };
    return fetch(REST_API_URL + "/sessions", params);
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
    const params = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify(newAccountData),
      credentials: "include",
    };
    return fetch(REST_API_URL + "/clients", params);
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
    const params = {
      method: "POST",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify(product),
      credentials: "include",
    };
    return fetch(REST_API_URL + "/baskets", params);
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
    const params = {
      method: "PUT",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify(product),
      credentials: "include",
    };
    return fetch(REST_API_URL + "/baskets", params);
  }

  static buyCartRequest(cart) {
    const params = {
      method: "POST",
      headers: {
        "Content-Type": "application/json;charset=utf-8",
      },
      body: JSON.stringify(cart),
      credentials: "include",
    };
    return fetch(REST_API_URL + "/purchases/baskets", params);
  }
}

export default DataService;
