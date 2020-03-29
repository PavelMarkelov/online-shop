import React from 'react';
import { connect } from 'react-redux';


export const FilterSidebar = props => {
    const parser = new DOMParser();
    const decodedChar = parser.parseFromString('&horbar;' ,'text/html').body.textContent;
    return (
        <div className="card sidebar-card my-3">
            <div className="card-body">
                <h5 className="card-title">Filters</h5>
                <div className="d-inline font-weight-bold">In stock:</div>
                <div className="d-inline"><input className="ml-2 align-middle"
                                                 type="checkbox" id="inStockCheck"/></div>
                <hr/>
                    <p className="font-weight-bold">Price range:</p>
                    <form>
                        <div className="form-row">
                            <div className="col-5">
                                <input type="number" className="form-control form-control-sm price-range" min="1"/>
                            </div>
                            <div className="col text-center">
                                <p>{ decodedChar }</p>
                            </div>
                            <div className="col-5">
                                <input type="number" className="form-control form-control-sm price-range" min="1"/>
                            </div>
                        </div>
                    </form>
                <hr/>
                <div className="row">
                    <div className="col-6">
                        <button style={ {width: '100%'} } type="button"
                                className="btn btn-primary btn-sm btn-success">Apply
                        </button>
                    </div>
                    <div className="col-6">
                        <button style={ {width: '100%'} } type="button" className="btn btn-secondary btn-sm">Reset
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
};





