import React from "react";
import './pop-up.css';

export const PopUp = props => {

    return (
        <div className="overlay">
            <div className="block-popup">
                <div id="warning" className="alert alert-danger text-center" role="alert">
                    { props.message || 'Oops! Network error...' }
                </div>
                <span>&times;</span>
            </div>
        </div>
    )
};