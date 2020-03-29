import React, { Component} from "react";
import book from "../../../images/icons/book.svg";
import cart from "../../../images/icons/cart-svgrepo-com.svg";
import arrowLeft from "../../../images/icons/box-arrow-left.svg";
import person from "../../../images/icons/person.svg";
import { Link } from "react-router-dom";
import DataService from '../../../service/DataService';
import { logoutUser } from '../../../actions/AccountActions'
import { connect } from "react-redux";
import { withRouter } from 'react-router-dom';

class AuthorizedNavbar extends Component {

    constructor (props) {
        super(props);
        this.handleLogout = this.handleLogout.bind(this);
    }

    async handleLogout(event) {
        event.preventDefault();
        await DataService.logoutRequest();
        this.props.logoutUser();
        this.props.history.push('/');
    }

    render() {
        return (
            <div className="navbar-nav ml-auto">
                <Link className="nav-item nav-link" to="/catalog">
                    <img src={ book } alt="catalog" width="32"/>
                    Catalog
                </Link>
                <Link className="nav-item nav-link" to="#">
                    <img src={ cart } alt="cart" width="30" className="p-1"/>
                    Cart
                </Link>
                <Link className="nav-item nav-link" onClick={ this.handleLogout } to="/">
                    <img src={ arrowLeft } alt="logout" width="32"
                         height="32"/>
                    Log Out
                </Link>
                <Link className="nav-item nav-link" to="/account">
                    <img src={person} alt="account" width="32" height="32"/>
                    { this.props.userFirstName }
                </Link>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        userFirstName: state.userState.user.firstName
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        logoutUser: () => dispatch(logoutUser())
    }
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(AuthorizedNavbar));