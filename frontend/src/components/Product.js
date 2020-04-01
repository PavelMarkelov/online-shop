import React, { Component } from "react";
import DataService from "../service/DataService";
import { productDescription } from "../actions/ProductActions";
import { connect } from 'react-redux';

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

    handleSubmit(event) {
        event.preventDefault();

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
                    <form name="add-to-cart">
                        <div className="row">

                            <div className="col-5">
                                <input className="form-control form-control-sm count-for-cart" type="number"
                                       name="count"
                                       required min="1"/>
                            </div>
                            <div className="col-7">
                                <button style={ {width: "80%"} } type="button" name="count"
                                        className="btn btn-primary btn-sm btn-success"
                                        onClick={ this.handleSubmit }>
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
        productDescription: product => dispatch(productDescription(product))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Product);