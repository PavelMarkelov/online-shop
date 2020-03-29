import React, {Component} from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import './components/templates/navbar/NavbarContainer'
import NavbarContainer from "./components/templates/navbar/NavbarContainer";
import Footer from "./components/templates/Footer";
import Login from './components/Login';
import Account from "./components/Account";
import PrivateRoute from "./components/PrivateRoute";
import BreadCrumbs from "./components/templates/BreadCrumbs";
import CatalogContainer from './components/catalog/CatalogContainer'

class App extends Component {
    render() {
        return (
            <BrowserRouter>
                <div className="flex-fill">
                    <NavbarContainer/>
                    <BreadCrumbs/>
                    <Switch>
                        <Route exact path="/" component={ Login }/>
                        <PrivateRoute path="/account" component={ Account }/>
                        <PrivateRoute path="/catalog" component={ CatalogContainer }/>
                    </Switch>
                    <Footer/>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;
