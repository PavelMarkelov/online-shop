import React from "react";
import ReportForm from "./ReportForm";

const Report = () => {
  return (
    <div className="row mx-0 mt-4">
      <div className="col-md-3 mr-5">
        <ReportForm onSubmit={(values) => alert(JSON.stringify(values))} />
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
              <th scope="row">1</th>
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
