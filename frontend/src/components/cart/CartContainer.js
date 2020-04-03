import React, { Component } from 'react';
import DataService from "../../service/DataService";
import {
    userCart,
    removeCartItem,
} from "../../actions/CartActions";
import { connect } from 'react-redux';
import CartItem from './CartItem'


class CartContainer extends Component {

    constructor(props) {
        super(props);
        this.handleCheckout = this.handleCheckout.bind(this);
    }

    async componentDidMount() {
        const response = await DataService.cartRequest();
        const cart = await response.json();
        this.props.userCart(cart);
    }
    

    async handleCheckout() {
        const cartWithoutImages = this.props.cart
            .map(value => Object.assign({}, {...value, image: undefined}));
        const response = await DataService.buyCartRequest(cartWithoutImages);
        const remainingCart = await response.json();
        this.props.userCart(remainingCart.remaining);
    }

    render() {
        let totalSum = this.props.cart
            .map(value => value.count * value.price)
            .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
        totalSum = (totalSum)
            .toLocaleString("en-US",{useGrouping:true});

        const productsInCart = this.props.cart
            .map(value => <CartItem key={ value.id } product={ value }
                                    remove={ this.props.removeCartItem } 
                                    userCart={ this.props.userCart }/>);

        return (

            productsInCart.length ?

            <div className="ml-5">
                { productsInCart }
                <div className="row mt-4">
                    <div className="col-sm-3">
                        <h5>Total: <strong className="text-success">${ totalSum }</strong></h5>
                    </div>
                    <div className="col-sm-4 pr-4">
                        <button onClick={ this.handleCheckout } type="button" className="mr-5 btn btn-warning float-right">Checkout</button>
                    </div>
                </div>
            </div>

                    :

                <div className="empty-cart">
                    <h4 className="text-muted">Your shopping cart is empty</h4>
                </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        cart: state.cartState.cart
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        userCart: (cart) => dispatch(userCart(cart)),
        removeCartItem: (id) => dispatch(removeCartItem(id)),
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CartContainer);