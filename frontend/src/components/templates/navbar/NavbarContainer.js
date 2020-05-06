import React, { Component } from "react";
import { Link } from "react-router-dom";
import logo from "../../../images/logo.png";
import { connect } from "react-redux";
import AuthorizedNavbar from "./AuthorizedNavbar";
import { UnauthorizedNavbar } from "./UnauthorizedNavbar";

class NavbarContainer extends Component {
  render() {
    return (
      <nav className="navbar navbar-expand-lg navbar-light">
        <Link className="navbar-brand" to="/catalog">
          <img
            src={logo}
            width="32"
            className="d-inline-block align-top mr-2"
            alt="logo"
          />
          Online-shop
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-toggle="collapse"
          data-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon" />
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          {this.props.isAuthenticated ? (
            <AuthorizedNavbar />
          ) : (
            <UnauthorizedNavbar />
          )}
        </div>
      </nav>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    isAuthenticated: state.userState.isAuthenticated,
  };
};

export default connect(mapStateToProps)(NavbarContainer);
