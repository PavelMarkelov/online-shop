import React, { useCallback } from "react";
import {
  fetchChangeQuantity,
  fetchRemoveItem,
  fetchUserCart,
} from "../../actions/CartActions";
import { shallowEqual, useDispatch, useSelector } from "react-redux";
import CartItem from "./CartItem";
import _ from "lodash";
import { animated, useTransition } from "react-spring";

const CartContainer = () => {
  const { editingCart, cachedCart } = useSelector(
    (state) => ({
      editingCart: state.cartState.editingCart,
      cachedCart: state.cartState.cachedCart,
    }),
    shallowEqual
  );

  const dispatch = useDispatch();
  const { removeItem, checkoutCart, changeQuantity } = {
    removeItem: (id, editingCart, cachedCart) =>
      dispatch(fetchRemoveItem(id, editingCart, cachedCart)),
    checkoutCart: async (cart) => dispatch(fetchUserCart(cart)),
    changeQuantity: useCallback(
      async (product) => dispatch(fetchChangeQuantity(product)),
      [dispatch]
    ),
  };

  async function handleCheckout() {
    const cartWithoutImages = editingCart.map((value) => {
      const newValue = { ...value };
      delete newValue.image;
      return newValue;
    });
    await checkoutCart(cartWithoutImages);
  }

  const handleRemove = useCallback(
    (id) => {
      const cartAfterRemoveItem = _.reject(editingCart, { id });
      removeItem(id, cartAfterRemoveItem, cachedCart);
    },
    [cachedCart, editingCart]
  );

  let totalSum = editingCart
    .map((value) => value.count * value.price)
    .reduce((previousValue, currentValue) => previousValue + currentValue, 0);
  totalSum = totalSum.toLocaleString("en-US", { useGrouping: true });

  const transitions = useTransition(editingCart, (item) => item.id, {
    from: { opacity: 0, transition: "all 0.3s ease", visibility: "hidden" },
    enter: { opacity: 1, transition: "all 0.3s ease", visibility: "visible" },
    leave: { opacity: 0, transition: "all 0.3s ease", visibility: "hidden" },
  });

  const productsInCart = transitions.map(({ item, key, props }) => (
    <animated.div key={key} style={props}>
      <CartItem
        product={item}
        onChangeQuantity={changeQuantity}
        onRemoveItem={handleRemove}
      />
    </animated.div>
  ));

  return productsInCart.length ? (
    <div className="ml-5">
      {productsInCart}
      <div className="row mt-4 m-0">
        <div className="col-sm-3">
          <h5>
            Total:{" "}
            <strong className="text-success" data-test="total-sum">
              ${totalSum}
            </strong>
          </h5>
        </div>
        <div className="col-sm-4 pr-4">
          <button
            onClick={handleCheckout}
            type="button"
            className="mr-5 btn btn-success float-right"
          >
            Checkout
          </button>
        </div>
      </div>
    </div>
  ) : (
    <div className="empty-cart">
      <h4 className="text-muted" data-test="empty-cart">
        Your shopping cart is empty
      </h4>
    </div>
  );
};

export default CartContainer;
