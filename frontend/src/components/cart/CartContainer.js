import React, { Component } from 'react';
import DataService from "../../service/DataService";
import { userCart } from "../../actions/CartActions";
import { connect } from 'react-redux';
import CartItem from './CartItem'


class CartContainer extends Component {

    async componentDidMount() {
        const response = await DataService.cartRequest();
        const cart = await response.json();
        this.props.userCart(cart);
    }

    render() {
        const totalSum = this.props.cart
            .map(value => value.count * value.price)
            .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
        const productsInCart = this.props.cart
            .map(value => <CartItem key={ value.id } product={ value }/>);
        return (
            <div className="ml-5">
                { productsInCart }
                <div className="row mt-4">
                    <div className="col-sm-3">
                        <h5>Total: ${ totalSum }</h5>
                    </div>
                    <div className="col-sm-4 pr-4">
                        <button className="mr-5 btn btn-warning float-right">Checkout</button>
                    </div>
                </div>
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
        userCart: (cart) => dispatch(userCart(cart))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CartContainer);