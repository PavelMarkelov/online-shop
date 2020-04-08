import React, {useCallback} from 'react';
import DataService from "../../service/DataService";
import {addItemsInUserCartAction, removeCartItemAction,} from "../../actions/CartActions";
import {useDispatch, useSelector} from 'react-redux';
import CartItem from './CartItem'


const CartContainer = () => {

    const cart = useSelector(state => state.cartState.cart);

    const dispatch = useDispatch();
    const { addItemsInUserCart, removeCartItem } = {
        addItemsInUserCart: useCallback(cart => dispatch(addItemsInUserCartAction(cart)), [dispatch]),
        removeCartItem: useCallback(productId => dispatch(removeCartItemAction(productId)), [dispatch])
    };
    
    async function handleCheckout() {
        const cartWithoutImages = cart
            .map(value => {
                const newValue = {...value};
                delete newValue.image;
                return newValue;
            });
        const response = await DataService.buyCartRequest(cartWithoutImages);
        const remainingCart = await response.json();
        addItemsInUserCart(remainingCart.remaining);
    }

    let totalSum = cart
        .map(value => value.count * value.price)
        .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
    totalSum = (totalSum)
        .toLocaleString("en-US",{useGrouping:true});

    const productsInCart = cart
        .map(value => <CartItem key={ value.id } product={ value }
                                remove={ removeCartItem }
                                addItemsInUserCart={ addItemsInUserCart }/>);

    return (

        productsInCart.length ?

            <div className="ml-5">
                { productsInCart }
                <div className="row mt-4">
                    <div className="col-sm-3">
                        <h5>Total: <strong className="text-success">${ totalSum }</strong></h5>
                    </div>
                    <div className="col-sm-4 pr-4">
                        <button onClick={ handleCheckout } type="button" className="mr-5 btn btn-warning float-right">Checkout</button>
                    </div>
                </div>
            </div>

            :

            <div className="empty-cart">
                <h4 className="text-muted">Your shopping cart is empty</h4>
            </div>
    )
};

export default CartContainer;