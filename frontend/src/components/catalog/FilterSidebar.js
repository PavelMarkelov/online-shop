import React from 'react';
import DataService from "../../service/DataService";


const FilterSidebar = (props) => {

    function handleSubmit(event) {
        event.preventDefault();
        const filtersForm = document.forms['filters-form'];
        const isInStock = filtersForm.elements['inStockCheck'].checked;
        const minPrice = +filtersForm.elements['minPrice'].value;
        const maxPrice = +filtersForm.elements['maxPrice'].value;
        const filter = {
            isInStock,
            minPrice,
            maxPrice
        };
        const isEmptyForm = !filter.isInStock && !filter.minPrice && !filter.maxPrice;
        const isInvalidRange = filter.minPrice && filter.maxPrice
            && filter.minPrice > filter.maxPrice;
        if (isEmptyForm || isInvalidRange)
            return false;
        props.enableFilter(filter);
    }

    async function handleDisableFilter() {
        const response = await DataService.productsListRequest();
        const products = await response.json();
        props.disableFilter(products);
    }

    const parser = new DOMParser();
    const decodedChar = parser.parseFromString('&horbar;' ,'text/html').body.textContent;
    return (
        <div className="card sidebar-card my-3">
            <div className="card-body">
                <h5 className="card-title">Filters</h5>
                <form name="filters-form" onSubmit={ handleSubmit }>
                    <div className="d-inline font-weight-bold">In stock:</div>
                    <div className="d-inline">
                        <input className="ml-2 align-middle"
                               type="checkbox" name="inStockCheck"/>
                    </div>
                    <hr/>
                    <p className="font-weight-bold">Price range:</p>
                    <div className="form-row">
                        <div className="col-5">
                            <input type="number" className="form-control form-control-sm price-range"
                                   min="1" name="minPrice"/>
                        </div>
                        <div className="col text-center">
                            <p>{ decodedChar }</p>
                        </div>
                        <div className="col-5">
                            <input type="number" className="form-control form-control-sm price-range"
                                   min="1" name="maxPrice"/>
                        </div>
                    </div>
                    <hr/>
                    <div className="row">
                        <div className="col-6">
                            <button style={ {width: '100%'} } type="submit"
                                    className="btn btn-primary btn-sm btn-success">Apply
                            </button>
                        </div>
                        <div className="col-6">
                            <button style={ {width: '100%'} } type="reset"
                                    className="btn btn-secondary btn-sm"
                                    onClick={ handleDisableFilter }>Reset
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    )
};

export default FilterSidebar;





