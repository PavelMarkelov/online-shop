import React from "react";

const Report = () => {
  const parser = new DOMParser();
  const decodedChar = parser.parseFromString("&horbar;", "text/html").body
    .textContent;

  return (
    <div className="row mx-0 mt-4">
      <div className="col-md-3 mr-5">
        <form>
          <p className="font-weight-bold">Price range:</p>
          <div className="form-row">
            <div className="col-5">
              <input
                type="number"
                min="0"
                className="form-control form-control-md"
                placeholder="min"
              />
            </div>
            <div className="col text-center">
              <p className="mt-1">{decodedChar}</p>
            </div>
            <div className="col-5">
              <input
                type="number"
                min="0"
                className="form-control form-control-md"
                placeholder="max"
              />
            </div>
            <div className="invalid-feedback">range err</div>
          </div>
          <div className="form-group mt-3">
            <label>
              <b>Email:</b>
            </label>
            <input
              type="text"
              className="form-control form-control-md"
              name="email"
              placeholder="Email"
            />
            <div className="invalid-feedback">email err</div>
          </div>
          <div className="form-check form-check-inline mr-3">
            <input type="checkbox" className="form-check-input mt-1" />
            <label className="form-check-label">Out of stock</label>
          </div>
          <div className="form-check form-check-inline m-0">
            <input type="checkbox" className="form-check-input mt-1" />
            <label className="form-check-label">Send to email</label>
          </div>
          <div className="col-md-12 p-0">
            <button type="submit" className="btn btn-md btn-primary my-3">
              Get report
            </button>
          </div>
        </form>
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
