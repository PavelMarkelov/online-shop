import React from "react";

export const ParentCategoryWithChildren = (props) => {
  return (
    <li className="mb-2" data-test="parent">
      {props.parentCategory}
      <ul className="categories-list hidden-children-categories">
        {props.childrenCategories}
      </ul>
    </li>
  );
};
