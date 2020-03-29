import React, { Component } from "react";
import { Link } from "react-router-dom";
import { connect } from 'react-redux';


class CategorySidebar extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
                <div className="card sidebar-card">
                    <div className="card-body">
                        <h5 className="card-title">Categories</h5>
                        <ul className="categories-list">
                            <li>
                                <Link to="#" className="card-link">Категория</Link>
                                <ul className="categories-list">
                                    <li>
                                        <Link to="#" className="card-link">Подкатегория</Link>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
        )
    }
}

export default CategorySidebar;