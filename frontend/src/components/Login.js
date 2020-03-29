import React, { Component } from "react";
import { Link } from "react-router-dom";
import { connect } from "react-redux";
import { loginUser } from '../actions/AccountActions';
import DataService from '../service/DataService';
import { withRouter } from 'react-router-dom';


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
        const user = await response.json();
        this.props.loginUser(user);
        this.props.history.push('/catalog');
    }

    render() {
        return (
            <div className="login-container">
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

const mapDispatchToProps = (dispatch) => {
    return {
        loginUser: (user) => dispatch(loginUser(user))
    }
};

export default withRouter(connect(null, mapDispatchToProps)(Login));