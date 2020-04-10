import React from "react";
import './pop-up.css';
import { useSelector, useDispatch, shallowEqual } from "react-redux";
import { toggleVisibilityAction } from "../../../actions/PopUpActions";

const PopUp = () => {

    const { isVisible, message } = useSelector(state => ({
        isVisible: state.popUpState.isVisible,
        message: state.popUpState.message
    }), shallowEqual);

    const dispatch = useDispatch();
    const toggleVisibility = isVisible => dispatch(toggleVisibilityAction(isVisible));

    const handleClosePopUp = event => {
        event.preventDefault();
        const popUpWindow = event.target.firstChild;
        if (popUpWindow.className === 'block-popup' || event.target.tagName === 'BUTTON')
            toggleVisibility(!isVisible);
    };

    return (
        <div className="overlay" style={ {display: isVisible ? 'block' : 'none'} }
             onClick={ handleClosePopUp }>
            <div className="block-popup">
                <div id="warning" className="alert alert-danger text-center" role="alert">
                    { message || 'Oops! Network error...' }
                </div>
                <button style={ {width: '30%'} } type="button"
                        className="mt-2 btn btn-primary btn-sm btn-success">Close
                </button>
            </div>
        </div>
    )
};

export default PopUp;