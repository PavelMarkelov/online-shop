import React, { useEffect } from 'react';
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
import { useSelector, useDispatch, shallowEqual } from "react-redux";


const CatalogContainer = () => {

    const { categories, products, filters } = useSelector(state => ({
        categories: state.categoriesState.categoriesList,
        products: state.productState.productsList,
        filters: state.productState.filter
    }), shallowEqual);

    const dispatch = useDispatch();
    const { categoriesList, productsList, productsCategory, enableFilter, disableFilter } = {
            categoriesList: categories => dispatch(categoriesListAction(categories)),
            productsList: products => dispatch(productsListAction(products)),
            productsCategory: products => dispatch(productsFromCategoryAction(products)),
            enableFilter: filter => dispatch(enableFilterAction(filter)),
            disableFilter: products => dispatch(disableFilterAction(products))
        };

    useEffect(() => {
        (async () => {
            document.documentElement.scrollIntoView();
            const responses = await Promise.all([
                DataService.productsListRequest(),
                DataService.categoriesListRequest()
            ]);
            const data = await Promise.all(responses.map(response => response.json()));
            productsList(data[0]);
            categoriesList(data[1]);
        })();
        return () => productsList([])
    }, []);

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