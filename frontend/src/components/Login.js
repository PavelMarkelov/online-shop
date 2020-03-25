import React, { Component } from "react";
import { Link } from "react-router-dom";
import BreadCrumbs from "./templates/BreadCrumbs";

class Login extends Component {
    breadCrumbsLink = new Map([["Log in", ""]]);

    render() {
        return (
            <div>
                <BreadCrumbs links={ this.breadCrumbsLink }/>
            <div className="login-container">
                <form>
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
                    <div className="form-group">
                        <button type="submit" className="btn btn-lg btn-primary btn-block">Log In</button>
                    </div>
                </form>
                <div id="sampleLogin">
                    <a href="#">customer</a>
                </div>
            </div>
            </div>
        );
    }
}

export default Login;