import React, { Component } from "react";
import DataService from "../../service/DataService";
import { Link } from "react-router-dom";


class CartItem extends Component {

    constructor(props) {
        super(props);

    }

    render() {
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
                                { this.props.product.count * this.props.product.price }
                            </p>
                            <form>
                                <div className="ml-1 form-group row">
                                    <p><strong>Count:</strong></p>
                                    <div className="col-sm-4">
                                        <input className="form-control form-control-sm" min="1"
                                               defaultValue={ this.props.product.count }
                                               type="number"
                                               required
                                               name="count"/>
                                    </div>
                                    <div className="col-sm-6">
                                        <Link to="#">Remove</Link>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        )
    }

}

export default CartItem;