import React from "react";
import {Link} from "react-router-dom";
import trash from '../../images/icons/trash.svg';
import {debounce} from 'lodash';


const CartItem = (props) => {

    function handleRemove(event) {
        event.preventDefault();
        props.onRemoveItem(props.product.id)
    }

    async function handleChangeQuantity(count) {
        const product = { ...props.product, count };
        delete product.image;
        props.onChangeQuantity(product);
    }

    const debouncedUpdate = debounce(count => handleChangeQuantity(count), 500);

    const total = (props.product.count * props.product.price)
        .toLocaleString("en-US",{useGrouping:true});
    return (
        <div className="card mb-3 border-dark cart-item">
            <div className="row no-gutters">
                <div className="col-md-4">
                    <img src={ props.product.image }
                         className="card-img p-2" alt={ props.product.name }/>
                </div>
                <div className="col-md-8">
                    <div className="card-body pb-0">
                        <h5 className="ml-1 card-title mb-4">{ props.product.name }</h5>
                        <p className="ml-1 card-text"><strong>Price: </strong>
                            <span className="text-success">${ total }</span>
                        </p>
                        <div className="ml-1 form-group row">
                            <p className="text-center mt-2"><strong>Count:</strong></p>
                            <div className="col-sm-4">
                                <input className="form-control form-control-sm mt-2" min="1"
                                       defaultValue={ props.product.count }
                                       type="number"
                                       required
                                       name="count" onChange={ ({ target: { value } }) => debouncedUpdate(value) }/>
                            </div>
                            <div className="col-sm-6">
                                <Link to="#" onClick={ handleRemove } className="btn btn-light">
                                    <img src={ trash } alt="trash" width="32"/>
                                </Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
};

export default CartItem;