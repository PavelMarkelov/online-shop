import React, {Component} from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import './components/templates/navbar/NavbarContainer'
import NavbarContainer from "./components/templates/navbar/NavbarContainer";
import Footer from "./components/templates/Footer";
import Login from './components/Login';
import Account from "./components/Account";
import PrivateRoute from "./components/PrivateRoute";

class App extends Component {
    render() {
        return (
            <BrowserRouter>
                <div className="flex-fill">
                    <NavbarContainer/>
                    <Switch>
                        <Route exact path="/" component={ Login }/>
                        <PrivateRoute exact path="/account" component={ Account }/>
                    </Switch>
                    <Footer/>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;
