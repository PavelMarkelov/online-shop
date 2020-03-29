import React from "react";
import { Link } from 'react-router-dom';
import { withRouter } from 'react-router-dom';

const BreadCrumbs = (props) => {
    const linksArr = props.location.pathname.split('/').slice(1);
    const lastIndex = linksArr.length - 1;
    linksArr.forEach((value, index) => {
        if (value && index !== lastIndex)
            linksArr[index] =
                <li key={value} className="breadcrumb-item">
                    <Link to={'/' + value}>{value[0].toUpperCase() + value.slice(1)}</Link>
                </li>;
        else if (value && index === lastIndex)
            linksArr[index] =
                <li key={ value } className="breadcrumb-item active">
                    {value[0].toUpperCase() + value.slice(1)}
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

export default withRouter(BreadCrumbs);