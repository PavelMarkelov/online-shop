import React, { useEffect, useCallback } from 'react';
import CategorySidebar from './CategorySidebar';
import FilterSidebar from "./FilterSidebar";
import {ProductItem} from "./ProductItem";
import DataService from "../../service/DataService";
import { categoriesListAction } from '../../actions/CategoriesActions';
import {
    disableFilterAction,
    enableFilterAction,
    productsFromCategoryAction,
    productsListAction,
} from "../../actions/ProductActions";
import { addItemsInUserCartAction } from "../../actions/CartActions";
import { useSelector, useDispatch, shallowEqual } from "react-redux";


const CatalogContainer = () => {

    const { categories, products, filters, isCartIsLoaded } = useSelector(state => ({
        categories: state.categoriesState.categoriesList,
        products: state.productState.productsList,
        filters: state.productState.filter,
        isCartIsLoaded: state.cartState.isCartIsLoaded
    }), shallowEqual);

    const dispatch = useDispatch();
    const { categoriesList, productsList, productsCategory, enableFilter, disableFilter, addItemsInUserCart } = {
        categoriesList: categories => dispatch(categoriesListAction(categories)),
        productsList: products => dispatch(productsListAction(products)),
        addItemsInUserCart: cart => dispatch(addItemsInUserCartAction(cart)),
        productsCategory: useCallback(products =>
            dispatch(productsFromCategoryAction(products)), [dispatch]),
        enableFilter: useCallback(filter =>
            dispatch(enableFilterAction(filter)), [dispatch]),
        disableFilter: useCallback(products =>
            dispatch(disableFilterAction(products)), [dispatch])
    };

    const loadDataWithCart = async () => {
        document.documentElement.scrollIntoView();
        const responses = await Promise.all([
            DataService.productsListRequest(),
            DataService.categoriesListRequest(),
            DataService.cartRequest()
        ]);
        const data = await Promise.all(responses.map(response => response.json()));
        productsList(data[0]);
        categoriesList(data[1]);
        addItemsInUserCart(data[2])
    };

    const loadDataWithoutCart = async () => {
        document.documentElement.scrollIntoView();
        const responses = await Promise.all([
            DataService.productsListRequest(),
            DataService.categoriesListRequest(),
        ]);
        const data = await Promise.all(responses.map(response => response.json()));
        productsList(data[0]);
        categoriesList(data[1]);
    };

    useEffect(() => {
        let time = performance.now();
        isCartIsLoaded ? loadDataWithoutCart() : loadDataWithCart();
        time = performance.now() - time;
        console.log('Время выполнения = ', time);
        return () => productsList([])
    }, [isCartIsLoaded]);

    const _filterProducts = (products, { isInStock, minPrice, maxPrice}) => {
        return products.filter(item => {
            if (isInStock && !item.count)
                return false;
            if (minPrice && item.price < minPrice)
                return false;
            if (maxPrice && item.price > maxPrice)
                return false;
            return true;
        })
    };

    const filteredProducts = filters ?
        _filterProducts(products, filters) : products;
    const productsForRender = filteredProducts.map(item =>
        <ProductItem key={ item.id } product={ item }/>
    );

    return (
        <div className="row">
            <div className="col-md-3 ml-4">
                <CategorySidebar categoriesList={ categories }
                                 productsCategory={ productsCategory }/>
                <FilterSidebar enableFilter={ enableFilter }
                               disableFilter={ disableFilter }/>
            </div>
            <div className="col-md-8">
                <div className="row row-cols-1 row-cols-md-3 text-center">
                    { productsForRender }
                </div>
            </div>
        </div>
    )
};

export default CatalogContainer;