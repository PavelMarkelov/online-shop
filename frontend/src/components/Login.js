import React, { Component } from "react";
import { Link } from "react-router-dom";
import { connect } from "react-redux";
import { loginUser } from '../actions/AccountActions';
import DataService from '../service/DataService';
import { withRouter } from 'react-router-dom';
import { loginError } from "../actions/ErrorActions";


class Login extends Component {

    simpleCustomer = {login: 'q', password: 'sddsvwe34s'};

    constructor(props) {
        super(props);
        this.handleSimpleCustomer = this.handleSimpleCustomer.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSimpleCustomer(event) {
        event.preventDefault();
        const loginForm = document.forms["login-form"];
        loginForm.elements["login"].value = this.simpleCustomer.login;
        loginForm.elements["password"].value = this.simpleCustomer.password;
    };

    async handleSubmit(event) {
        event.preventDefault();
        const loginForm = document.forms["login-form"];
        const login = loginForm.elements["login"].value;
        const password = loginForm.elements["password"].value;
        const response = await DataService.loginRequest({login, password});
        if (response.status === 401) {
            this.props.loginError(true);
            return false;
        }
        this.props.loginError(false);
        const user = await response.json();
        this.props.loginUser(user);
        this.props.history.push('/catalog');
    }

    render() {
        const styleForVisibility = this.props.isLoginFail ? {visibility: 'visible'} : { visibility: 'hidden'};
        return (
            <div className="login-container">
                <div id="warning" className="alert alert-danger text-center py-1 mb-2" role="alert"
                     style={ styleForVisibility }>
                    Invalid login or password!
                </div>
                <form name="login-form" onSubmit={ this.handleSubmit } >
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
                    <Link onClick={ this.handleSimpleCustomer } to="#">customer</Link>
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        isLoginFail: state.errorState.isLoginFail
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        loginUser: (user) => dispatch(loginUser(user)),
        loginError: (isLoginFail) => dispatch(loginError(isLoginFail))
    }
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Login));