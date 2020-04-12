import React, {useCallback} from 'react';
import {fetchChangeQuantity, fetchRemoveItem, fetchUserCart,} from "../../actions/CartActions";
import {useDispatch, useSelector, shallowEqual} from 'react-redux';
import CartItem from './CartItem';
import _ from 'lodash';

const CartContainer = () => {

    const { editingCart, cachedCart } = useSelector(state => ({
        editingCart: state.cartState.editingCart,
        cachedCart: state.cartState.cachedCart
    }), shallowEqual);

    const dispatch = useDispatch();
    const { removeItem, checkoutCart, changeQuantity } = {
        removeItem: (id, editingCart, cachedCart) => dispatch(fetchRemoveItem(id, editingCart, cachedCart)),
        checkoutCart: async cart => dispatch(fetchUserCart(cart)),
        changeQuantity: useCallback(async product => dispatch(fetchChangeQuantity(product)),[dispatch])
    };

    async function handleCheckout() {
        const cartWithoutImages = editingCart
            .map(value => {
                const newValue = {...value};
                delete newValue.image;
                return newValue;
            });
        await checkoutCart(cartWithoutImages);
    }
    
    const handleRemove = useCallback(id => {
        const cartAfterRemoveItem = _.reject(editingCart, { id });
        removeItem(id, cartAfterRemoveItem, cachedCart);
    }, [cachedCart, editingCart]);

    let totalSum = editingCart
        .map(value => value.count * value.price)
        .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
    totalSum = (totalSum)
        .toLocaleString("en-US",{useGrouping:true});

    const productsInCart = editingCart
        .map(value => <CartItem key={ value.id } product={ value }
                                onChangeQuantity={ changeQuantity }
                                onRemoveItem={ handleRemove }/>);

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