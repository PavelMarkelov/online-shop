import React, { useRef } from "react";

const FilterSidebar = (props) => {
  const isInStockInput = useRef(null);
  const minPriceInput = useRef(null);
  const maxPriceInput = useRef(null);

  function handleSubmit(event) {
    event.preventDefault();
    const isInStock = isInStockInput.current.checked;
    const minPrice = +minPriceInput.current.value;
    const maxPrice = +maxPriceInput.current.value;
    const isEmptyForm = !isInStock && !minPrice && !maxPrice;
    const isInvalidRange = minPrice > maxPrice;
    if (isEmptyForm || isInvalidRange) return false;
    props.enableFilter({ isInStock, minPrice, maxPrice });
  }

  const parser = new DOMParser();
  const decodedChar = parser.parseFromString("&horbar;", "text/html").body
    .textContent;
  return (
    <div className="card sidebar-card my-3">
      <div className="card-body">
        <h5 className="card-title">Filters</h5>
        <form
          ref={props.filtersFormRef}
          onSubmit={handleSubmit}
          name="filters-form"
        >
          <div className="d-inline font-weight-bold">In stock:</div>
          <div className="d-inline">
            <input
              className="ml-2 align-middle"
              type="checkbox"
              ref={isInStockInput}
              data-test="is-in-stock"
            />
          </div>
          <hr />
          <p className="font-weight-bold">Price range:</p>
          <div className="form-row">
            <div className="col-5">
              <input
                type="number"
                className="form-control form-control-sm price-range"
                defaultValue={props.minPrice}
                ref={minPriceInput}
                data-test="min-price"
              />
            </div>
            <div className="col text-center">
              <p>{decodedChar}</p>
            </div>
            <div className="col-5">
              <input
                type="number"
                className="form-control form-control-sm price-range"
                defaultValue={props.maxPrice}
                ref={maxPriceInput}
                data-test="max-price"
              />
            </div>
          </div>
          <hr />
          <div className="row">
            <div className="col-6">
              <button
                style={{ width: "100%" }}
                type="submit"
                className="btn btn-primary btn-sm btn-success"
              >
                Apply
              </button>
            </div>
            <div className="col-6">
              <button
                style={{ width: "100%" }}
                type="reset"
                className="btn btn-secondary btn-sm"
                onClick={props.onDisableFilter}
              >
                Reset
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default FilterSidebar;
