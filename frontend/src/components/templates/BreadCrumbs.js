import React from "react";
import { Link } from "react-router-dom";
import { withRouter } from "react-router-dom";
import { connect } from "react-redux";

export const BreadCrumbs = (props) => {
  const product = props.product.name || " ";
  let linksArr = props.location.pathname.split("/").slice(1);
  const lastIndex = linksArr.length - 1;
  let addressLink = "";
  linksArr = linksArr.map((value, index) => {
    if (value) {
      addressLink += "/" + value;
      value = isNaN(value) ? value : product;
      value = value[0].toUpperCase() + value.slice(1);
      if (index !== lastIndex)
        return (
          <li
            key={value}
            className="breadcrumb-item"
            data-testid={"li-" + value}
          >
            <Link to={addressLink}>{value}</Link>
          </li>
        );
      return (
        <li
          key={value}
          className="breadcrumb-item active"
          data-testid={"li-" + value}
        >
          {value}
        </li>
      );
    }
    return (
      <li key="login" className="breadcrumb-item active" data-testid="li-login">
        Log in
      </li>
    );
  });
  return (
    <nav aria-label="breadcrumb">
      <ol className="breadcrumb">{linksArr}</ol>
    </nav>
  );
};

const mapStateToProps = (state) => {
  return {
    product: state.productState.productDetails,
  };
};

export default withRouter(connect(mapStateToProps)(BreadCrumbs));
