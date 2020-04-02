import React, { Component } from "react";
import DataService from "../service/DataService";
import { productDescription } from "../actions/ProductActions";
import { connect } from 'react-redux';
import { userCart } from "../actions/CartActions";

class Product extends Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        const request = await DataService.productRequest(this.props.match.params.id);
        const product = await request.json();
        this.props.productDescription(product);
    }

    componentWillUnmount() {
        this.props.productDescription({});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const countForm = document.forms['add-to-cart'];
        const count = +countForm.elements['count'].value;
        const {id, name, price} = this.props.product;
        debugger
        const productForRequest = {
            id,
            name,
            price,
            count
        };
        const response = await DataService.addToCartRequest(productForRequest);
        const cart = await response.json();
        this.props.userCart(cart);
    }

    render() {
        let stockLevel = 'none';
        const count = this.props.product.count;
        if (count)
            stockLevel = count < 10 ? 'few' : 'much';
        return (
            <div className="row row-cols-2 mx-3">
                <div className="col-sm-3">
                    <h5>{ this.props.product.name }</h5>
                    <img src={ this.props.product.image }
                         className="img-fluid mt-2" alt={ this.props.product.name }/>
                    <p className="mt-2 mb-1"><strong>Price: </strong>${ this.props.product.price }</p>
                    <p><strong>Stock level: </strong>{ stockLevel }</p>
                    <form name="add-to-cart" onSubmit={ this.handleSubmit }>
                        <div className="row">

                            <div className="col-5">
                                <input className="form-control form-control-sm count-for-cart" type="number"
                                       name="count" min="1"/>
                            </div>
                            <div className="col-7">
                                <button style={ {width: "80%"} } type="submit"
                                        className="btn btn-primary btn-sm btn-success">
                                    Add to Cart
                                </button>
                            </div>

                        </div>
                    </form>
                </div>

                <div className="col-sm-9">
                    <p className="font-weight-bold">Description</p>
                    <p className="text-justify">{ this.props.product.description }</p>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        product: state.productState.productDescription
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        productDescription: product => dispatch(productDescription(product)),
        userCart: (cart) => dispatch(userCart(cart))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Product);