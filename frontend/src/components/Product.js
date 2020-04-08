import React, {useEffect} from "react";
import DataService from "../service/DataService";
import {productDetailsAction} from "../actions/ProductActions";
import {useDispatch, useSelector} from 'react-redux';
import {addItemsInUserCartAction} from "../actions/CartActions";

const Product = (props) => {

    const product = useSelector(state => state.productState.productDetails);

    const dispatch = useDispatch();
    const { productDetails, addItemsInUserCart } = {
        productDetails: product => dispatch(productDetailsAction(product)),
        addItemsInUserCart: (cart) => dispatch(addItemsInUserCartAction(cart))
    };

    useEffect(() => {
        (async () =>{
            const request = await DataService.productRequest(props.match.params.id);
            const product = await request.json();
            productDetails(product);
        })();
        return (() => productDetails({}))
    }, [props.match.params.id]);

    async function handleSubmit(event) {
        event.preventDefault();
        const countForm = document.forms['add-to-cart'];
        const count = +countForm.elements['count'].value;
        const {id, name, price} = product;
        const productForRequest = {
            id,
            name,
            price,
            count
        };
        const response = await DataService.addToCartRequest(productForRequest);
        if (!response.ok)
            return false;
        const cart = await response.json();
        addItemsInUserCart(cart);
    }

    let stockLevel = 'none';
    const count = product.count;
    if (count)
        stockLevel = count < 10 ? 'few' : 'much';
    return (
        <div className="row row-cols-2 mx-3">
            <div className="col-sm-3">
                <h5>{ product.name }</h5>
                <img src={ product.image }
                     className="img-fluid mt-2" alt={ product.name }/>
                <p className="mt-2 mb-1"><strong>Price: </strong>${ product.price }</p>
                <p><strong>Stock level: </strong>{ stockLevel }</p>
                <form name="add-to-cart" onSubmit={ handleSubmit }>
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
                <p className="text-justify">{ product.description }</p>
            </div>
        </div>
    )
};

export default Product;