import React, { useCallback } from "react";
import ReportForm from "./ReportForm";
import { useDispatch, useSelector } from "react-redux";
import { fetchGetReport } from "../../actions/ReportActions";

const Report = () => {
  const products = useSelector((state) => state.reportState.products);

  const dispatch = useDispatch();

  const handleGetReport = useCallback(
    (formValues) => {
      const requestValues = {};
      requestValues.minCount = +formValues.minCount;
      requestValues.maxCount = +formValues.maxCount;
      if (formValues.isOutOfStock) {
        requestValues.minCount = 0;
        requestValues.maxCount = 0;
      }
      requestValues.email = formValues.isSentToEmail ? formValues.email : "";
      dispatch(fetchGetReport(requestValues, formValues.isSentToEmail));
    },
    [dispatch]
  );

  const productsForRender = products.map((product) => {
    const price = product.price.toLocaleString("en-US", {
      useGrouping: true,
    });
    return (
      <tr key={product.id}>
        <th scope="row">{product.id}</th>
        <td>{product.name}</td>
        <td>{product.count}</td>
        <td className="px-3">${price}</td>
        <td>{product.categories.join(", ")}</td>
      </tr>
    );
  });

  return (
    <div className="row mx-0 mt-4">
      <div className="col-md-3">
        <ReportForm onSubmit={handleGetReport} />
      </div>

      <div className="col-md-9 mt-2 d-flex justify-content-center">
        <table style={{ width: "90%" }} className="table table-hover">
          <thead>
            <tr className="table-success">
              <th scope="col">Id</th>
              <th scope="col">Name</th>
              <th scope="col">Quantity</th>
              <th scope="col" className="px-3">
                Price
              </th>
              <th scope="col">Categories</th>
            </tr>
          </thead>
          <tbody>
            {productsForRender.length ? (
              productsForRender
            ) : (
              <tr>
                <th scope="row">0</th>
                <td>Name</td>
                <td>Quantity</td>
                <td>Price</td>
                <td>Categories</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Report;
