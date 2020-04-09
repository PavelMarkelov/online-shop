import React from "react";
import {Link, withRouter} from "react-router-dom";
import {useDispatch} from "react-redux";
import {fetchLoginUser} from "../actions/AccountActions";
import {PopUp} from "./templates/pop-up/PopUp";

const Login = (props) => {

    const simpleCustomer = {login: 'q', password: 'sddsvwe34s'};

    const dispatch = useDispatch();
    const loginUser = credentials => dispatch(fetchLoginUser(credentials));

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
        loginUser({ login, password });
        props.history.push('/catalog');
    }

    return (
        <div className="login-container">
            <PopUp/>
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