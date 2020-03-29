import React, { Component } from 'react';
import CategorySidebar from './CategorySidebar';
import { connect } from 'react-redux';
import { FilterSidebar } from "./FilterSidebar";
import { Link } from "react-router-dom";
import { ProductItem } from "./ProductItem";


class CatalogContainer extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div className="row">
                <div className="col-md-3 ml-4">
                    <CategorySidebar/>
                    <FilterSidebar/>
                </div>
                <div className="col-md-8">
                    <div className="row d-inline-flex text-center">
                        <ProductItem/>
                    </div>
                </div>
            </div>
        )
    }
}

export default CatalogContainer;