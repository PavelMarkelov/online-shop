import React from "react";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { loginUserAction } from '../actions/AccountActions';
import DataService from '../service/DataService';
import { withRouter } from 'react-router-dom';
import { loginErrorAction } from "../actions/ErrorActions";

const Login = (props) => {

    const simpleCustomer = {login: 'q', password: 'sddsvwe34s'};

    const isLoginFail = useSelector(state => state.errorState.isLoginFail);

    const dispatch = useDispatch();
    const { loginError, loginUser } = {
        loginError: (isLoginFalse) => dispatch(loginErrorAction(isLoginFalse)),
        loginUser: user => dispatch(loginUserAction(user))
    };

    function handleSimpleCustomer(event) {
        event.preventDefault();
        const loginForm = document.forms["login-form"];
        loginForm.elements["login"].value = simpleCustomer.login;
        loginForm.elements["password"].value = simpleCustomer.password;
    }

    async function handleSubmit(event) {
        event.preventDefault();
        const loginForm = document.forms["login-form"];
        const login = loginForm.elements["login"].value;
        const password = loginForm.elements["password"].value;
        const response = await DataService.loginRequest({login, password});
        if (response.status === 401) {
            loginError(true);
            return false;
        }
        loginError(false);
        const user = await response.json();
        loginUser(user);
        props.history.push('/catalog');
    }

    const styleForVisibility = isLoginFail ? {visibility: 'visible'} : { visibility: 'hidden'};
    return (
        <div className="login-container">
            <div id="warning" className="alert alert-danger text-center py-1 mb-2" role="alert"
                 style={ styleForVisibility }>
                Invalid login or password!
            </div>
            <form name="login-form" onSubmit={ handleSubmit } >
                <div className="form-group">
                    <label>Login</label>
                    <input type="text" className="form-control form-control-lg" id="login" placeholder="Enter login"
                           name="login" autoComplete="on" required autoFocus/>
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" className="form-control form-control-lg" id="password" autoComplete="on"
                           name="password" placeholder="Enter password" required/>
                </div>
                <div className="form-group mt-4">
                    <button type="submit" className="btn btn-lg btn-primary btn-block">Log In</button>
                </div>
            </form>
            <div id="sampleLogin">
                <Link onClick={ handleSimpleCustomer } to="#">customer</Link>
            </div>
        </div>
    );
};

export default withRouter(Login);