import React, { useCallback, useEffect } from "react";
import CategorySidebar from "./CategorySidebar";
import FilterSidebar from "./FilterSidebar";
import { ProductItem } from "./ProductItem";
import {
  disableFilterAction,
  enableFilterAction,
  fetchProductFromCategory,
  productsListAction,
} from "../../actions/ProductActions";
import { loadDataAction } from "../../actions/CatalogActions";
import { shallowEqual, useDispatch, useSelector } from "react-redux";
import { max, min } from "lodash";
import { animated, useSpring } from "react-spring";

const CatalogContainer = () => {
  const {
    categories,
    products,
    cachedProducts,
    filters,
    isCartIsLoaded,
    role,
  } = useSelector(
    (state) => ({
      categories: state.categoriesState.categoriesList,
      products: state.productState.productsList,
      cachedProducts: state.productState.cachedProductList,
      filters: state.productState.filter,
      isCartIsLoaded: state.cartState.isCartIsLoaded,
      role: state.userState.user.role,
    }),
    shallowEqual
  );
  const dispatch = useDispatch();
  const {
    loadData,
    productsList,
    productsCategory,
    enableFilter,
    disableFilter,
  } = {
    loadData: async (isCartIsLoaded, role) =>
      dispatch(loadDataAction(isCartIsLoaded, role)),
    productsList: (products) => dispatch(productsListAction(products)),
    productsCategory: useCallback(
      async (categoryId) => dispatch(fetchProductFromCategory(categoryId)),
      [dispatch]
    ),
    enableFilter: useCallback(
      (filter) => dispatch(enableFilterAction(filter)),
      [dispatch]
    ),
    disableFilter: (cachedProducts) =>
      dispatch(disableFilterAction(cachedProducts)),
  };

  const props = useSpring({
    from: { opacity: 0, transition: "all 0.5s ease", visibility: "hidden" },
    to: { opacity: 1, transition: "all 0.5s ease", visibility: "visible" },
  });

  useEffect(() => {
    document.documentElement.scrollIntoView();
    loadData(isCartIsLoaded, role);
    return () => productsList([]);
  }, []);

  const filterProducts = (products, { isInStock, minPrice, maxPrice }) => {
    return products.filter((item) => {
      if (isInStock && !item.count) return false;
      if (minPrice && item.price < minPrice) return false;
      if (maxPrice && item.price > maxPrice) return false;
      return true;
    });
  };

  const handleDisableFilter = useCallback(() => disableFilter(cachedProducts), [
    cachedProducts,
    disableFilter,
  ]);

  const pricesArray = [];
  const filteredProducts = filters
    ? filterProducts(products, filters)
    : products;

  const productsForRender = filteredProducts.map((item) => {
    pricesArray.push(item.price);
    return <ProductItem key={item.id} product={item} />;
  });

  const minPrice = min(pricesArray);
  const maxPrice = max(pricesArray);

  return (
    <div className="row m-0">
      <div className="col-md-3 ml-3">
        <CategorySidebar
          categoriesList={categories}
          productsCategory={productsCategory}
        />
        <FilterSidebar
          enableFilter={enableFilter}
          onDisableFilter={handleDisableFilter}
          minPrice={minPrice}
          maxPrice={maxPrice}
        />
      </div>
      <div className="col-md-8">
        <animated.div style={props}>
          <div className="row row-cols-1 row-cols-md-3 text-center">
            {productsForRender}
          </div>
        </animated.div>
      </div>
    </div>
  );
};

export default CatalogContainer;
