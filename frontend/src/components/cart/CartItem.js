import React, { Component } from "react";
import DataService from "../../service/DataService";
import { Link } from "react-router-dom";
import trash from '../../images/icons/trash.svg';


class CartItem extends Component {

    constructor(props) {
        super(props);
        this.handleRemove = this.handleRemove.bind(this);
        this.handleChangeQuantity = this.handleChangeQuantity.bind(this);
    }

    handleRemove(event) {
        event.preventDefault();
        DataService.removeCartItemRequest(this.props.product.id);
        this.props.remove(this.props.product.id);
    }

    async handleChangeQuantity(event) {
        const count = event.currentTarget.value;
        const product = Object.assign({}, { ...this.props.product, count });
        delete product.image;
        const response = await DataService.changeCartItemQuantity(product);
        const cart = await response.json();
        this.props.userCart(cart);
    }

    render() {
        const total = (this.props.product.count * this.props.product.price)
            .toLocaleString("en-US",{useGrouping:true});
        return (
            <div className="card mb-3 border-dark cart-item">
                <div className="row no-gutters">
                    <div className="col-md-4">
                        <img src={ this.props.product.image }
                             className="card-img p-2" alt={ this.props.product.name }/>
                    </div>
                    <div className="col-md-8">
                        <div className="card-body pb-0">
                            <h5 className="ml-1 card-title">{ this.props.product.name }</h5>
                            <p className="ml-1 card-text"><strong>Price: </strong>
                                <span className="text-success">${ total }</span>
                            </p>
                            <div className="ml-1 form-group row">
                                <p className="m-0 text-center"><strong>Count:</strong></p>
                                <div className="col-sm-4">
                                    <input className="form-control form-control-sm" min="1"
                                           defaultValue={ this.props.product.count }
                                           type="number"
                                           required
                                           name="count" onChange={ this.handleChangeQuantity }/>
                                </div>
                                <div className="col-sm-6">
                                    <Link to="#" onClick={ this.handleRemove }>
                                        <img src={ trash } alt="trash" width="32"/>
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    }

}

export default CartItem;