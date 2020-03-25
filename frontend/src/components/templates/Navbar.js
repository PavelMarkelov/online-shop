import React, { Component } from "react";
import { Link } from "react-router-dom";
import logo from '../../images/logo.png';
import book from '../../images/icons/book.svg';
import arrowLeft from '../../images/icons/box-arrow-left.svg';
import arrowRight from '../../images/icons/box-arrow-right.svg';
import cart from '../../images/icons/cart-svgrepo-com.svg';
import person from '../../images/icons/person.svg';

class Navbar extends Component {

    render() {
        return (
            <nav className="navbar navbar-expand-lg navbar-light">
                <a className="navbar-brand" href="#">
                    <img src={ logo } width="32"
                         className="d-inline-block align-top" alt="logo"/>
                        Online-shop
                </a>
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"/>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <div className="navbar-nav ml-auto">
                        <a className="nav-item nav-link" href="#">
                            <img src={ book } alt="catalog" width="32"/>
                                Catalog
                        </a>
                        <a className="nav-item nav-link" href="">
                            <img src={ cart } alt="cart" width="30"/>
                                Cart
                        </a>
                        <a className="nav-item nav-link" href="">
                            <img src={ arrowRight } alt="login" width="32"/>
                                Log In
                        </a>
                        <a className="nav-item nav-link" href="">
                            <img src={ arrowLeft } alt="logout" width="32"
                                 height="32"/>
                                Log Out
                        </a>
                        <a className="nav-item nav-link" href="">
                            <img src={ person } alt="account" width="32" height="32"/>
                                Account
                        </a>
                    </div>
                </div>
            </nav>
        );
    }
}

export default Navbar;