import React, { Component } from "react";
import { Link } from "react-router-dom";
import DataService from '../../service/DataService'


class CategorySidebar extends Component {

    constructor(props) {
        super(props);
        this.handleProductsCategory = this.handleProductsCategory.bind(this);
    }

    async handleProductsCategory(id, event) {
        event.preventDefault();
        const response = await DataService.productsCategoryRequest(id);
        const products = await response.json();
        document.forms['filters-form'].reset();
        this.props.productsCategory(products);
    }

    render() {
        const categoriesForRender = this.props.categoriesList.map(value => {
            if (!value.parentId) {
                let childrenCategories = this.props.categoriesList
                    .filter(children => value.id === children.parentId);
                childrenCategories = childrenCategories.map(children =>
                    <ul key={children.id} className="categories-list">
                        <li>
                            <Link to="#" onClick={ (event) => this.handleProductsCategory(children.id, event) }
                                  className="card-link">{children.name}</Link>
                        </li>
                    </ul>
                );
                return (
                    <li key={ value.id } className="mb-2">
                        <Link to="#" onClick={ (event) => this.handleProductsCategory(value.id, event) }
                              className="card-link">{ value.name }</Link>
                        { childrenCategories }
                    </li>
                )
            }
            return null;
        });

        return (
            <div className="card sidebar-card">
                <div className="card-body">
                    <h5 className="card-title mb-4">Categories</h5>
                    <ul className="categories-list">
                        { categoriesForRender }
                    </ul>
                </div>
            </div>
        )
    }
}

export default CategorySidebar;