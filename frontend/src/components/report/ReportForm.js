import React from "react";
import { Field, formValueSelector, reduxForm } from "redux-form";
import { connect, useSelector } from "react-redux";

let ReportForm = (props) => {
  const {
    handleSubmit,
    handleDownloadReport,
    isLoading,
    pristine,
    reset,
    submitting,
  } = props;

  const selector = formValueSelector("reportForm");

  const {
    minCount,
    maxCount,
    email,
    isOutOfStock,
    isSentToEmail,
  } = useSelector((state) => {
    return selector(
      state,
      "minCount",
      "maxCount",
      "email",
      "isOutOfStock",
      "isSentToEmail"
    );
  });

  const parser = new DOMParser();
  const decodedChar = parser.parseFromString("&horbar;", "text/html").body
    .textContent;
  const min = +minCount,
    max = +maxCount;

  const isHiddenMessageError = !isOutOfStock ? min <= max : true;

  const hasErrorEmail =
    isSentToEmail && !/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/.test(email);

  const hasRangeError = !isOutOfStock && (min < 0 || max < 0 || min > max);
  const hasFieldErrors = hasRangeError || hasErrorEmail;

  return (
    <form onSubmit={handleSubmit}>
      <p className="font-weight-bold mb-2">Quantity range:</p>
      <div className="form-row">
        <div className="col-5">
          <Field
            type="number"
            component="input"
            className={`form-control form-control-md ${
              min < 0 && !isOutOfStock && "is-invalid"
            }`}
            name="minCount"
          />
          <div className="invalid-feedback">Invalid min</div>
        </div>
        <div className="col text-center">
          <p className="mt-1">{decodedChar}</p>
        </div>
        <div className="col-5">
          <Field
            type="number"
            component="input"
            className={`form-control form-control-md ${
              max < 0 && !isOutOfStock && "is-invalid"
            }`}
            name="maxCount"
          />
          <div className="invalid-feedback">Invalid max</div>
        </div>
      </div>
      <div className="error-message text-center" hidden={isHiddenMessageError}>
        Invalid range
      </div>
      <div className="form-group mt-3">
        <label>
          <b>Email:</b>
        </label>
        <Field
          type="text"
          component="input"
          className={`form-control form-control-md ${
            hasErrorEmail && "is-invalid"
          }`}
          name="email"
          placeholder="Email"
        />
        <div className="invalid-feedback">Invalid format e-mail</div>
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
          autoComplete="off"
        />
        <label className="form-check-label">Send to email</label>
      </div>
      <div className="row">
        <div className="col-md-6 p-0 text-center">
          <button
            type="submit"
            className="btn btn-md btn-primary mt-3 w-75"
            disabled={pristine || submitting || hasFieldErrors}
          >
            Get report
          </button>
        </div>
        <div className="col-md-6 p-0 text-center">
          <button
            type="button"
            className="btn btn-md btn-secondary mt-3 w-75"
            disabled={pristine || submitting}
            onClick={reset}
          >
            Reset
          </button>
        </div>
      </div>
      <hr />
      <div className="text-center">
        <button
          type="button"
          className="btn btn-md btn-primary my-3 btn-block"
          disabled={pristine || submitting || hasRangeError}
          onClick={() => handleDownloadReport(min, max, isOutOfStock)}
        >
          {isLoading ? (
            <>
              <span className="mr-3">Downloading...</span>
              <span
                className="spinner-border spinner-border-sm"
                role="status"
                aria-hidden="true"
              />
            </>
          ) : (
            "Download"
          )}
        </button>
      </div>
    </form>
  );
};

ReportForm = reduxForm({
  form: "reportForm",
})(ReportForm);

ReportForm = connect((state) => ({
  initialValues: state.reportState.formData,
}))(ReportForm);

export default ReportForm;
