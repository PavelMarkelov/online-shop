import React from "react";
import { Field, reduxForm } from "redux-form";

const ReportForm = (props) => {
  const { handleSubmit, pristine, reset, submitting } = props;

  const parser = new DOMParser();
  const decodedChar = parser.parseFromString("&horbar;", "text/html").body
    .textContent;

  return (
    <form onSubmit={handleSubmit}>
      <p className="font-weight-bold mb-2">Price range:</p>
      <div className="form-row">
        <div className="col-5">
          <Field
            type="number"
            component="input"
            min="0"
            className="form-control form-control-md"
            placeholder="min"
            name="minCount"
          />
        </div>
        <div className="col text-center">
          <p className="mt-1">{decodedChar}</p>
        </div>
        <div className="col-5">
          <Field
            type="number"
            component="input"
            min="0"
            className="form-control form-control-md"
            placeholder="max"
            name="maxCount"
          />
        </div>
        <div className="invalid-feedback">range err</div>
      </div>
      <div className="form-group mt-3">
        <label>
          <b>Email:</b>
        </label>
        <Field
          type="text"
          component="input"
          className="form-control form-control-md"
          name="email"
          placeholder="Email"
        />
        <div className="invalid-feedback">email err</div>
      </div>
      <div className="form-check form-check-inline mr-3">
        <Field
          type="checkbox"
          className="form-check-input mt-1"
          component="input"
          name="isOutOfStock"
        />
        <label className="form-check-label">Out of stock</label>
      </div>
      <div className="form-check form-check-inline m-0">
        <Field
          type="checkbox"
          className="form-check-input mt-1"
          component="input"
          name="isSentToEmail"
        />
        <label className="form-check-label">Send to email</label>
      </div>
      <div className="row">
        <div className="col-md-6 p-0 text-center">
          <button
            type="submit"
            className="btn btn-md btn-primary my-3"
            disabled={pristine || submitting}
            style={{ width: "80%" }}
          >
            Get report
          </button>
        </div>
        <div className="col-md-6 p-0 text-center">
          <button
            type="button"
            style={{ width: "80%" }}
            className="btn btn-md btn-secondary my-3"
            disabled={pristine || submitting}
            onClick={reset}
          >
            Reset
          </button>
        </div>
      </div>
    </form>
  );
};

export default reduxForm({
  form: "reportForm",
})(ReportForm);
