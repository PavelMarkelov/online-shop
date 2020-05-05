import React from "react";
import { useHistory } from "react-router-dom";
import { shallowEqual, useDispatch, useSelector } from "react-redux";
import {
  fetchLoginUser,
  submitCredentialsAction,
} from "../actions/AccountActions";

const Login = () => {
  const user = {
    customerLogin: "q",
    adminLogin: "qefg",
    password: "sddsvwe34s",
  };

  const history = useHistory();

  const { login, password, isRememberMe } = useSelector(
    (state) => ({
      login: state.userState.login,
      password: state.userState.password,
      isRememberMe: state.userState.isRememberMe,
    }),
    shallowEqual
  );

  const dispatch = useDispatch();

  const { loginUser, submitCredentials } = {
    loginUser: async (credentials) => dispatch(fetchLoginUser(credentials)),
    submitCredentials: (credentials) =>
      dispatch(submitCredentialsAction(credentials)),
  };

  const loginForm = { login, password, isRememberMe };

  function handleSimpleUser(event) {
    event.preventDefault();
    document.forms["login-form"].reset();
    const credentials = { password: user.password };
    const target =
      event.target.tagName === "P" ? event.target.parentNode : event.target;
    credentials.login =
      target.id === "customer" ? user.customerLogin : user.adminLogin;
    submitCredentials(credentials);
  }

  function handleInputChange(event) {
    const target = event.target;
    target.type !== "checkbox"
      ? (loginForm[target.name] = target.value)
      : (loginForm[target.name] = target.checked);
    submitCredentials(loginForm);
  }

  async function handleSubmit(event) {
    event.preventDefault();
    await loginUser(loginForm);
    history.push("/catalog");
  }

  return (
    <div className="login-container">
      <form name="login-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Login</label>
          <input
            type="text"
            className="form-control form-control-lg"
            placeholder="Enter login"
            name="login"
            autoComplete="on"
            required
            autoFocus
            defaultValue={loginForm.login}
            onChange={handleInputChange}
          />
        </div>
        <div className="form-group">
          <label>Password</label>
          <input
            type="password"
            className="form-control form-control-lg"
            autoComplete="on"
            name="password"
            placeholder="Enter password"
            required
            defaultValue={loginForm.password}
            onChange={handleInputChange}
          />
        </div>
        <div className="form-group form-check">
          <input
            type="checkbox"
            className="form-check-input mt-2"
            name="isRememberMe"
            defaultChecked={loginForm.isRememberMe}
            onChange={handleInputChange}
          />
          <label className="form-check-label">Remember me</label>
        </div>
        <div className="form-group mt-4">
          <button type="submit" className="btn btn-lg btn-primary btn-block">
            Log In
          </button>
        </div>
      </form>
      <div className="row">
        <div className="col-md-3">
          <button
            id="customer"
            type="button"
            className="btn btn-light"
            onClick={handleSimpleUser}
          >
            <p className="text-center pb-1 m-0">customer</p>
          </button>
        </div>
        <div className="col-md-3">
          <button
            id="admin"
            type="button"
            className="btn btn-light"
            onClick={handleSimpleUser}
          >
            <p className="text-center pb-1 m-0">admin</p>
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;
