import React, {Component} from 'react';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import './components/templates/Navbar'
import Navbar from "./components/templates/Navbar";
import Footer from "./components/templates/Footer";
import Login from './components/Login';

class App extends Component {
    render() {
        return (
            <BrowserRouter>
                <div className="flex-fill">
                    <Navbar/>
                    <Switch>
                        <Route exact path="/" component={ Login }/>
                    </Switch>
                    <Footer/>
                </div>
            </BrowserRouter>
        );
    }
}

export default App;
