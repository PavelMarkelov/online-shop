import React from "react";
import "./pop-up.css";
import { useSelector, useDispatch, shallowEqual } from "react-redux";
import { toggleVisibilityAction } from "../../../actions/PopUpActions";

const PopUp = () => {
  const {
    isVisible,
    failMessage,
    successMessage,
    loadingMessage,
  } = useSelector(
    (state) => ({
      isVisible: state.popUpState.isVisible,
      failMessage: state.popUpState.messageForFail,
      successMessage: state.popUpState.messageForSuccess,
      loadingMessage: state.popUpState.messageForLoading,
    }),
    shallowEqual
  );

  const dispatch = useDispatch();
  const toggleVisibility = (isVisible) =>
    dispatch(toggleVisibilityAction(isVisible));

  const handleClosePopUp = (event) => {
    event.preventDefault();
    const popUpWindow = event.target.firstChild;
    if (
      popUpWindow.className === "block-popup" ||
      event.target.tagName === "BUTTON"
    )
      toggleVisibility(!isVisible);
  };

  return (
    <div
      className={isVisible ? "overlay pop-up-visible" : "overlay"}
      onClick={handleClosePopUp}
    >
      <div className="block-popup">
        {failMessage && (
          <div className="alert alert-danger text-center" role="alert">
            {failMessage}
          </div>
        )}
        {successMessage && (
          <div className="alert alert-success" role="alert">
            {successMessage}
          </div>
        )}
        {loadingMessage && (
          <div className="alert alert-secondary m-0" role="alert">
            <div className="row">
              <div className="col-md-6 p-0 mt-1">
                <strong className="float-right">{loadingMessage}</strong>
              </div>
              <div className="col-md-6">
                <div
                  className="spinner-border text-info float-right mr-4"
                  role="status"
                  aria-hidden="true"
                />
              </div>
            </div>
          </div>
        )}
        {!loadingMessage && (
          <button
            type="button"
            className="mt-2 btn btn-primary btn-sm btn-success w-25"
          >
            Close
          </button>
        )}
      </div>
    </div>
  );
};

export default PopUp;
