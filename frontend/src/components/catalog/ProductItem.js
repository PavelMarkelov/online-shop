import React from "react";
import {Link} from "react-router-dom";


export const ProductItem = props => {

    return (
        <div className="col-md-6 col-lg-4 border border-info">
            <img height="50%" className="card-img-top pt-3"
                 src="https://main-cdn.goods.ru/big1/hlr-system/1748262/100000009691b0.jpg" alt="???????"/>
            <div className="card-body" style={ {lineHeight: 0.8} }>
                <h6 className="card-title">productName???</h6>
                <div className="text-left">
                    <p className="card-text"><strong>Price: </strong>productPrice???</p>
                    <p className="card-text"><strong>Stock: </strong>productStock???</p>
                </div>
            </div>
            <Link to="#" className="w-50 ml-5 btn btn-sm btn-primary btn-success btn-block">Description</Link>
        </div>
    )
};