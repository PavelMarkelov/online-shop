import React from "react";
import { Route, Redirect } from "react-router-dom";
import { connect } from "react-redux";
import userRole from "../userRole";

const PrivateRoute = ({ component: Component, auth, role, ...rest }) => (
  <Route
    {...rest}
    render={(props) => {
      if (auth) {
        if (role === userRole.CUSTOMER && rest.path === "/report")
          return <Redirect to="/catalog" />;
        else return <Component {...props} />;
      }
      return <Redirect to="/" />;
    }}
  />
);

const mapStateToProps = (state) => {
  return {
    auth: state.userState.isAuthenticated,
    role: state.userState.user.role,
  };
};

export default connect(mapStateToProps)(PrivateRoute);
