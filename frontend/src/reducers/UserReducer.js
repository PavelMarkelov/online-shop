import * as actionType from "../actions/ActionsType";

const initialState = {
  isAuthenticated: false,
  user: {},
  login: "",
  password: "",
};

const userReducer = (state = initialState, action) => {
  switch (action.type) {
    case actionType.LOGIN_USER:
      return {
        isAuthenticated: true,
        user: action.payload,
      };

    case actionType.LOGOUT_USER:
      return {
        isAuthenticated: false,
        user: action.payload,
      };

    case actionType.USER_ACCOUNT:
      return {
        isAuthenticated: true,
        user: action.payload,
      };

    case actionType.SUBMIT_CREDENTIALS:
      return {
        ...state,
        login: action.payload.login,
        password: action.payload.password,
      };

    default:
      return state;
  }
};

export default userReducer;
