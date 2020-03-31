import React from "react";
import {Link} from "react-router-dom";


export const ProductItem = props => {

    let stockLevel = 'out';
    const count = props.product.count;
    if (count)
        stockLevel = count < 10 ? 'few' : 'much';
    return (
        <div className="col mb-4">
            <div className="card">
                <img className="card-img-top pt-3" src={ props.product.image }
                     alt={ props.product.name }/>
                <div className="card-body pb-1 px-1">
                    <h6 className="card-title">{ props.product.name }</h6>
                    <div className="text-left" style={ {lineHeight: 0.9} }>
                        <p className="card-text"><strong>Price: </strong>${ props.product.price }</p>
                        <p className="card-text"><strong>Stock: </strong>{ stockLevel }</p>
                    </div>
                    <br/>
                    <Link to="#" className="w-50 ml-5 btn btn-sm btn-primary btn-success btn-block">Description</Link>
                </div>
            </div>
        </div>
    )
};