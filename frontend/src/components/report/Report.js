import React, { useCallback } from "react";
import ReportForm from "./ReportForm";
import { useDispatch } from "react-redux";
import { fetchGetReport } from "../../actions/ReportActions";

const Report = () => {
  const dispatch = useDispatch();

  const handleGetReport = useCallback(
    (formValues) => {
      const requestValues = {};
      requestValues.minCount = +formValues.minCount;
      requestValues.maxCount = +formValues.maxCount;
      if (formValues.isOutOfStock) {
        formValues.minCount = 0;
        formValues.maxCount = 0;
      }
      requestValues.email = formValues.isSentToEmail ? formValues.email : "";
      dispatch(fetchGetReport(requestValues, formValues.isSentToEmail));
    },
    [dispatch]
  );

  return (
    <div className="row mx-0 mt-4">
      <div className="col-md-3 mr-5">
        <ReportForm onSubmit={handleGetReport} />
      </div>

      <div className="col-md-7 mt-2">
        <table className="table">
          <thead>
            <tr className="table-success">
              <th scope="col">Id</th>
              <th scope="col">Name</th>
              <th scope="col">Count</th>
              <th scope="col">Price</th>
              <th scope="col">Categories</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th scope="row">0</th>
              <td>Name</td>
              <td className="count-cell">Count</td>
              <td>Price</td>
              <td>Categories</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Report;
