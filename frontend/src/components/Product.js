import React, { useEffect, useRef } from "react";
import { productDetailsAction } from "../actions/ProductActions";
import { useDispatch, useSelector } from "react-redux";
import { fetchAddItemsInCart } from "../actions/CartActions";
import _ from "lodash";

const Product = (props) => {
  const { product, productsList } = useSelector((state) => ({
    product: state.productState.productDetails,
    productsList: state.productState.productsList,
  }));

  const dispatch = useDispatch();
  const { productDetails, addItemsInCart } = {
    productDetails: async (product) => dispatch(productDetailsAction(product)),
    addItemsInCart: (cart) => dispatch(fetchAddItemsInCart(cart)),
  };

  useEffect(() => {
    const id = +props.match.params.id;
    const product = _.find(productsList, { id });
    productDetails(product);
  }, [props.match.params.id]);

  const countInput = useRef(null);

  async function handleSubmit(event) {
    event.preventDefault();
    const count = +countInput.current.value;
    const { id, name, price } = product;
    const productForRequest = {
      id,
      name,
      price,
      count,
    };
    await addItemsInCart(productForRequest);
  }

  let stockLevel = "none";
  const count = product.count;
  if (count) stockLevel = count < 10 ? "few" : "much";
  const price =
    product.price &&
    product.price.toLocaleString("en-US", { useGrouping: true });

  return (
    <div className="row row-cols-2 mx-3">
      <div className="col-sm-3">
        <h5>{product.name}</h5>
        <img
          src={product.image}
          className="img-fluid mt-2"
          alt={product.name}
        />
        <p className="mt-2 mb-1">
          <strong>Price: </strong>${price}
        </p>
        <p>
          <strong>Stock level: </strong>
          {stockLevel}
        </p>
        <form name="add-to-cart" onSubmit={handleSubmit}>
          <div className="row">
            <div className="col-5">
              <input
                className="form-control form-control-sm count-for-cart"
                type="number"
                min="1"
                defaultValue="1"
                ref={countInput}
              />
            </div>
            <div className="col-7">
              <button
                style={{ width: "80%" }}
                type="submit"
                className="btn btn-primary btn-sm btn-success"
              >
                Add to Cart
              </button>
            </div>
          </div>
        </form>
      </div>

      <div className="col-sm-9">
        <p className="font-weight-bold">Description</p>
        <p className="text-justify">{product.description}</p>
      </div>
    </div>
  );
};

export default Product;
