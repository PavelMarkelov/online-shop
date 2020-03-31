import React, { Component } from "react";
import { Link } from "react-router-dom";
import { connect } from 'react-redux';


class CategorySidebar extends Component {

    constructor(props) {
        super(props);
        this.handleProductsCategory = this.handleProductsCategory.bind(this);
    }

    handleProductsCategory() {

    }

    render() {
        const categoriesForRender = this.props.categoriesList.map(value => {
            if (!value.parentId) {
                let childrenCategories = this.props.categoriesList
                    .filter(children => value.id === children.parentId);
                childrenCategories = childrenCategories.map(children =>
                    <ul key={children.id} className="categories-list">
                        <li>
                            <Link to="#" onClick={ () => this.handleProductsCategory(children.id) }
                                  className="card-link">{children.name}</Link>
                        </li>
                    </ul>
                );
                return (
                    <li key={ value.id } className="mb-2">
                        <Link to="#" onClick={ () => this.handleProductsCategory(value.id) }
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