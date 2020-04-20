import React, {useCallback, useEffect} from 'react';
import CategorySidebar from './CategorySidebar';
import FilterSidebar from "./FilterSidebar";
import {ProductItem} from "./ProductItem";
import {
    disableFilterAction,
    enableFilterAction,
    productsFromCategoryAction,
    productsListAction
} from "../../actions/ProductActions";
import {loadDataAction} from "../../actions/CatalogActions";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {max, min} from 'lodash';
import {animated, useSpring} from "react-spring";


const CatalogContainer = () => {

    const { categories, products, filters, isCartIsLoaded } = useSelector(state => ({
        categories: state.categoriesState.categoriesList,
        products: state.productState.productsList,
        filters: state.productState.filter,
        isCartIsLoaded: state.cartState.isCartIsLoaded
    }), shallowEqual);

    const dispatch = useDispatch();
    const { loadData, productsList, productsCategory, enableFilter, disableFilter } = {
        loadData: async isCartIsLoaded => dispatch(loadDataAction(isCartIsLoaded)),
        productsList: products => dispatch(productsListAction(products)),
        productsCategory: useCallback(products =>
            dispatch(productsFromCategoryAction(products)), [dispatch]),
        enableFilter: useCallback(filter =>
            dispatch(enableFilterAction(filter)), [dispatch]),
        disableFilter: useCallback(products =>
            dispatch(disableFilterAction(products)), [dispatch])
    };

    const props = useSpring({
        from: { opacity: 0, transition: 'all 0.5s ease', visibility: 'hidden' },
        to: { opacity: 1, transition: 'all 0.5s ease', visibility: 'visible' }
    });

    useEffect(() => {
        document.documentElement.scrollIntoView();
        loadData(isCartIsLoaded);
        return () => productsList([])
    }, []);

    const filterProducts = (products, { isInStock, minPrice, maxPrice}) => {
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

    const pricesArray = [];
    const filteredProducts = filters ?
        filterProducts(products, filters) : products;

    const productsForRender = filteredProducts.map(item => {
            pricesArray.push(item.price);
            return (
                <ProductItem key={ item.id } product={ item }/>
            );
        }
    );

    const minPrice = min(pricesArray);
    const maxPrice = max(pricesArray);

    return (
        <div className="row">
            <div className="col-md-3 ml-4">
                <CategorySidebar categoriesList={ categories }
                                 productsCategory={ productsCategory }/>
                <FilterSidebar enableFilter={ enableFilter }
                               disableFilter={ disableFilter }
                               minPrice={ minPrice } maxPrice={ maxPrice }/>
            </div>
            <div className="col-md-8">
                <animated.div style={props}>
                    <div className="row row-cols-1 row-cols-md-3 text-center">
                        { productsForRender }
                    </div>
                </animated.div>
            </div>
        </div>
    )
};

export default CatalogContainer;