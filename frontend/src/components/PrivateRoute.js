import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { connect } from 'react-redux';

const PrivateRoute = ({ component: Component, auth, ...rest }) => (
    <Route
        {...rest}
        render={ props =>
            auth === true ? (
                <Component {...props}/>
            ) : (
                <Redirect to='/'/>
            )
        }
    />
);

const mapStateToProps = (state) => {
    return {
        auth: state.userState.isAuthenticated
    };
};

export default connect(mapStateToProps)(PrivateRoute);