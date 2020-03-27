import React, { Component} from "react";
import book from "../../../images/icons/book.svg";
import cart from "../../../images/icons/cart-svgrepo-com.svg";
import arrowLeft from "../../../images/icons/box-arrow-left.svg";
import person from "../../../images/icons/person.svg";
import { Link } from "react-router-dom";
import DataService from '../../../service/DataService';
import { logoutUser } from '../../../actions/Actions'
import { connect } from "react-redux";

class AuthorizedNavbar extends Component {

    constructor (props) {
        super(props);
        this.handleLogout = this.handleLogout.bind(this);
    }

    async handleLogout(event) {
        event.preventDefault();
        const req = await DataService.logoutRequest();
        const body = await req.text();
        console.log(body);
        this.props.logoutUser();
    }

    render() {
        return (
            <div className="navbar-nav ml-auto">
                <a className="nav-item nav-link" href="#">
                    <img src={ book } alt="catalog" width="32"/>
                    Catalog
                </a>
                <a className="nav-item nav-link" href="">
                    <img src={ cart } alt="cart" width="30" className="p-1"/>
                    Cart
                </a>
                <Link className="nav-item nav-link" onClick={ this.handleLogout } to="/">
                    <img src={ arrowLeft } alt="logout" width="32"
                         height="32"/>
                    Log Out
                </Link>
                <a className="nav-item nav-link" href="">
                    <img src={person} alt="account" width="32" height="32"/>
                    { this.props.userFirstName }
                </a>
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

export default connect(mapStateToProps, mapDispatchToProps)(AuthorizedNavbar);