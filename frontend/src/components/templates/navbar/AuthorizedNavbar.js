import React, { Component, Fragment } from "react";
import book from "../../../images/icons/book.svg";
import cart from "../../../images/icons/cart-svgrepo-com.svg";
import arrowLeft from "../../../images/icons/box-arrow-left.svg";
import person from "../../../images/icons/person.svg";
import { Link, withRouter } from "react-router-dom";
import { connect } from "react-redux";
import { fetchLogout } from "../../../actions/AccountActions";
import userRole from "../../../userRole";
import listTask from "../../../images/icons/list-task.svg";

class AuthorizedNavbar extends Component {
  constructor(props) {
    super(props);
    this.handleLogout = this.handleLogout.bind(this);
  }

  async handleLogout(event) {
    event.preventDefault();
    if (await this.props.logoutUser()) this.props.history.push("/");
  }

  render() {
    const badgeStatus = this.props.count ? "badge-success" : "badge-light";
    const role = this.props.user.role;
    return (
      <div className="navbar-nav ml-auto">
        <Link className="nav-item nav-link" to="/catalog">
          <img src={book} alt="catalog" width="32" />
          Catalog
        </Link>
        {role === userRole.CUSTOMER ? (
          <Fragment>
            <Link className="nav-item nav-link" to="/account/cart">
              <img src={cart} alt="cart" width="34" className="p-1" />
              Cart{" "}
              <span className={`badge ${badgeStatus}`}>{this.props.count}</span>
            </Link>
            <Link className="nav-item nav-link" to="/account">
              <img src={person} alt="account" width="32" height="32" />
              {this.props.user.firstName}
            </Link>
          </Fragment>
        ) : (
          <Fragment>
            <Link className="nav-item nav-link" to="/report">
              <img src={listTask} alt="report" width="32" height="32" />
              Report
            </Link>
            <div className="nav-item nav-link">
              <img src={person} alt="admin" width="32" height="32" />
              Admin
            </div>
          </Fragment>
        )}
        <Link
          className="nav-item nav-link ml-5"
          onClick={this.handleLogout}
          to="/"
        >
          <img src={arrowLeft} alt="logout" width="32" height="32" />
          Log Out
        </Link>
      </div>
    );
  }
}

const mapStateToProps = (state) => {
  return {
    count: state.cartState.editingCart.length,
    user: state.userState.user,
  };
};

const mapDispatchToProps = (dispatch) => {
  return {
    logoutUser: async () => dispatch(fetchLogout()),
  };
};

export default withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AuthorizedNavbar)
);
