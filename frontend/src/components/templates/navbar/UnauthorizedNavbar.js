import React from "react";
import { Link } from "react-router-dom";
import arrowRight from "../../../images/icons/box-arrow-right.svg";

export const UnauthorizedNavbar = () => {
  return (
    <div className="navbar-nav ml-auto">
      <Link to="/" className="nav-item nav-link">
        <img src={arrowRight} alt="login" width="32" />
        Log In
      </Link>
    </div>
  );
};
