import React, { useEffect, useState } from "react";
import { shallowEqual, useDispatch, useSelector } from "react-redux";
import { fetchAccountEdit, fetchUserAccount } from "../actions/AccountActions";
import { Link } from "react-router-dom";
import { accountEditErrorAction } from "../actions/ErrorActions";

const Account = (props) => {
  const password = "sddsvwe34s";

  const { account, error } = useSelector(
    (state) => ({
      account: state.userState.user,
      error: state.errorState.error,
    }),
    shallowEqual
  );

  const dispatch = useDispatch();
  const { accountEdit, accountEditError, userAccount } = {
    accountEdit: async (account) => dispatch(fetchAccountEdit(account)),
    accountEditError: (error) => dispatch(accountEditErrorAction(error)),
    userAccount: () => dispatch(fetchUserAccount()),
  };

  useEffect(() => {
    userAccount();
    return () => accountEditError(null);
  }, []);

  const [formValues, setFormValues] = useState({
    ...account,
    oldPassword: password,
    newPassword: password,
  });

  function handleInputChange(event) {
    const target = event.target;
    setFormValues({
      ...formValues,
      [target.name]: target.value,
    });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    const hasNoError = await accountEdit(formValues);
    if (hasNoError) props.history.push("/catalog");
  }

  const classesForInput = "form-control form-control-lg";
  const classesForInputWithAlert = "form-control form-control-lg is-invalid";
  const errorsFields = new Map();
  if (error) {
    if (error.errors)
      error.errors.forEach((item) =>
        errorsFields.set(item.field, item.message)
      );
    else errorsFields.set(error.field, error.message);
  }
  return (
    <div className="w-25 account-edit-container">
      <h1 className="m-4">Account</h1>
      <form name="edit-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>
            <b>Fist name</b>
          </label>
          <input
            type="text"
            className={
              errorsFields.get("firstName")
                ? classesForInputWithAlert
                : classesForInput
            }
            name="firstName"
            placeholder="Your first name"
            value={formValues.firstName}
            autoFocus
            onChange={handleInputChange}
          />
          <div className="invalid-feedback">
            {errorsFields.get("firstName")}
          </div>
        </div>
        <div className="form-group">
          <label>
            <b>Last name</b>
          </label>
          <input
            type="text"
            className={
              errorsFields.get("lastName")
                ? classesForInputWithAlert
                : classesForInput
            }
            name="lastName"
            placeholder="Your last name"
            value={formValues.lastName}
            onChange={handleInputChange}
          />
          <div className="invalid-feedback">{errorsFields.get("lastName")}</div>
        </div>
        <div className="form-group">
          <label>
            <b>Patronymic</b>
          </label>
          <input
            type="text"
            className={
              errorsFields.get("patronymic")
                ? classesForInputWithAlert
                : classesForInput
            }
            name="patronymic"
            placeholder="Your patronymic"
            value={formValues.patronymic}
            onChange={handleInputChange}
          />
          <div className="invalid-feedback">
            {errorsFields.get("patronymic")}
          </div>
        </div>
        <div className="form-group">
          <label>
            <b>Email</b>
          </label>
          <input
            type="email"
            className={
              errorsFields.get("email")
                ? classesForInputWithAlert
                : classesForInput
            }
            name="email"
            placeholder="Your email"
            value={formValues.email}
            onChange={handleInputChange}
          />
          <div className="invalid-feedback">{errorsFields.get("email")}</div>
        </div>
        <div className="form-group">
          <label>
            <b>Address</b>
          </label>
          <textarea
            className={
              errorsFields.get("address")
                ? classesForInputWithAlert
                : classesForInput
            }
            name="address"
            placeholder="Your address"
            value={formValues.address}
            onChange={handleInputChange}
            rows="2"
          />
          <div className="invalid-feedback">{errorsFields.get("address")}</div>
        </div>
        <div className="form-group">
          <label>
            <b>Phone</b>
          </label>
          <input
            type="text"
            className={
              errorsFields.get("phone")
                ? classesForInputWithAlert
                : classesForInput
            }
            name="phone"
            placeholder="Your phone"
            value={formValues.phone}
            onChange={handleInputChange}
          />
          <div className="invalid-feedback">{errorsFields.get("phone")}</div>
        </div>
        <div className="form-group">
          <label>
            <b>Old password</b>
          </label>
          <input
            type="password"
            className={
              errorsFields.get("oldPassword")
                ? classesForInputWithAlert
                : classesForInput
            }
            name="oldPassword"
            placeholder="Old password"
            value={formValues.oldPassword}
            onChange={handleInputChange}
          />
          <div className="invalid-feedback">
            {errorsFields.get("oldPassword")}
          </div>
        </div>
        <div className="form-group">
          <label>
            <b>New password</b>
          </label>
          <input
            type="password"
            className={
              errorsFields.get("newPassword")
                ? classesForInputWithAlert
                : classesForInput
            }
            name="newPassword"
            placeholder="New password"
            value={formValues.newPassword}
            onChange={handleInputChange}
          />
          <div className="invalid-feedback">
            {errorsFields.get("newPassword")}
          </div>
        </div>
        <div className="form-group mt-4 row">
          <div className="col-6">
            <button type="submit" className="btn btn-lg btn-success btn-block">
              Save
            </button>
          </div>
          <div className="col-6">
            <Link
              className="w-100 btn btn-lg btn-primary"
              to="/catalog"
              role="button"
            >
              Cancel
            </Link>
          </div>
        </div>
      </form>
    </div>
  );
};

export default Account;
