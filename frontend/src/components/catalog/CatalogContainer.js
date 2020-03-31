import React, { Component } from 'react';
import CategorySidebar from './CategorySidebar';
import { connect } from 'react-redux';
import FilterSidebar from "./FilterSidebar";
import { Link } from "react-router-dom";
import { ProductItem } from "./ProductItem";
import DataService from "../../service/DataService";
import { categoriesList } from '../../actions/CategoriesActions';
import { productsList } from "../../actions/ProductActions";
import { productsCategory } from "../../actions/ProductActions";
import { enableFilter } from "../../actions/ProductActions";
import { disableFilter } from "../../actions/ProductActions";

class CatalogContainer extends Component {

    async componentDidMount() {
        const responses = await Promise.all([
            DataService.categoriesListRequest(),
            DataService.productsListRequest()
        ]);
        const data = await Promise.all(responses.map(item => item.json()));
        this.props.categoriesList(data[0]);
        this.props.productsList(data[1]);
    }

    _filterProducts(products, { isInStock, minPrice, maxPrice}) {
        return products.filter(item => {
            if (isInStock && !item.count)
                return false;
            if (minPrice && item.price < minPrice)
                return false;
            if (maxPrice && item.price > maxPrice)
                return false;
            return true;
        })
    }

    render() {
        const filters = this.props.filters;
        const products = filters ?
            this._filterProducts(this.props.products, filters) : this.props.products;
        const productsForRender = products.map(item =>
            <ProductItem key={ item.id } product={ item }/>
            );
        return (
            <div className="row">
                <div className="col-md-3 ml-4">
                    <CategorySidebar categoriesList={ this.props.categories }
                                     productsCategory={ this.props.productsCategory }/>
                    <FilterSidebar enableFilter={ this.props.enableFilter }
                            disableFilter={ this.props.disableFilter }/>
                </div>
                <div className="col-md-8">
                    <div className="row row-cols-1 row-cols-md-3 text-center">
                        { productsForRender }
                    </div>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        categories: state.categoriesState.categoriesList,
        products: state.productState.productsList,
        filters: state.productState.filter
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        categoriesList: categories => dispatch(categoriesList(categories)),
        productsList: products => dispatch(productsList(products)),
        productsCategory: products => dispatch(productsCategory(products)),
        enableFilter: filter => dispatch(enableFilter(filter)),
        disableFilter: products => dispatch(disableFilter(products))
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(CatalogContainer);