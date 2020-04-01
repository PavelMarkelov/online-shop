import React from "react";
import { Link } from 'react-router-dom';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';

const BreadCrumbs = (props) => {
    const product = props.product.name || ' ';
    const linksArr = props.location.pathname.split('/').slice(1);
    const lastIndex = linksArr.length - 1;
    let addressLink = '';
    linksArr.forEach((value, index) => {
        addressLink += '/' + value;
        const name = !isNaN(value) ? product : value;
        if (value && index !== lastIndex)
            linksArr[index] =
                <li key={value} className="breadcrumb-item">
                    <Link to={ addressLink }>{value[0].toUpperCase() + value.slice(1)}</Link>
                </li>;
        else if (value && index === lastIndex)
            linksArr[index] =
                <li key={ value } className="breadcrumb-item active">
                    {name[0].toUpperCase() + name.slice(1)}
                </li>;
        else
            linksArr[index] = <li key='login' className="breadcrumb-item active">Log in</li>;
    });
    return (
        <nav aria-label="breadcrumb">
            <ol className="breadcrumb">
                { linksArr }
            </ol>
        </nav>
    );
};

const mapStateToProps = (state) => {
    return {
        product: state.productState.productDescription
    }
};

export default withRouter(connect(mapStateToProps)(BreadCrumbs));