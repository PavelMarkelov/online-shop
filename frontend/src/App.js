import React, {Component} from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import './components/templates/navbar/Navbar'
import Navbar from "./components/templates/navbar/Navbar";
import Footer from "./components/templates/Footer";
import Login from './components/Login';
import Account from "./components/Account";

class App extends Component {
    render() {
        return (
            <BrowserRouter>
                <div className="flex-fill">
                    <Navbar/>
                    <Switch>
                        <Route exact path="/" component={ Login }/>
                        <Route exact path="/account" component={ Account }/>
                    </Switch>
                    <Footer/>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;
