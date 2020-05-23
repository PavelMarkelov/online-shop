import React from "react";

export const ParentCategoryWithoutChildren = (props) => {
  return (
    <li className="mb-2" data-test="parent">
      {props.parentCategory}
    </li>
  );
};
