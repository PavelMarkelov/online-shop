import React from "react";
import { Link } from 'react-router-dom';

const BreadCrumbs = (props) => {

    let linksArr = [];
    props.links.forEach((val, key) => {
        if (val)
            linksArr.push(
                <li key={ key } className="breadcrumb-item">
                    <Link to={ val }>{ key }</Link>
                </li>
            );
        else
            linksArr.push(<li key={ key } className="breadcrumb-item active">{ key }</li>);
    });

        return (
            <nav aria-label="breadcrumb">
                <ol className="breadcrumb">
                    { linksArr }
                </ol>
            </nav>
        );
    };

export default BreadCrumbs;