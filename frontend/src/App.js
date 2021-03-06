import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";
import NavbarContainer from "./components/templates/navbar/NavbarContainer";
import Footer from "./components/templates/Footer";
import Login from "./components/Login";
import Account from "./components/Account";
import PrivateRoute from "./components/PrivateRoute";
import BreadCrumbs from "./components/templates/BreadCrumbs";
import CatalogContainer from "./components/catalog/CatalogContainer";
import Product from "./components/Product";
import CartContainer from "./components/cart/CartContainer";
import PopUp from "./components/templates/pop-up/PopUp";
import { useDispatch } from "react-redux";
import { loginUserAction } from "./actions/AccountActions";
import Report from "./components/report/Report";

const App = () => {
  const dispatch = useDispatch();

  if (localStorage.getItem("user")) {
    const user = JSON.parse(localStorage.getItem("user"));
    dispatch(loginUserAction(user));
  }

  return (
    <BrowserRouter>
      <div className="flex-fill">
        <NavbarContainer />
        <BreadCrumbs />
        <PopUp />
        <Switch>
          <Route exact path="/" component={Login} />
          <PrivateRoute exact path="/account" component={Account} />
          <PrivateRoute exact path="/catalog" component={CatalogContainer} />
          <PrivateRoute path="/catalog/:id" component={Product} />
          <PrivateRoute path="/account/cart" component={CartContainer} />
          <PrivateRoute path="/report" component={Report} />
        </Switch>
        <Footer />
      </div>
    </BrowserRouter>
  );
};

export default App;
