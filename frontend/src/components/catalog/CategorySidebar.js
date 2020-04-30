import React from "react";

const CategorySidebar = (props) => {
  const handleProductsCategory = async (id, event) => {
    event.preventDefault();
    const childrenCategoriesList = event.target.parentNode.querySelector("ul");
    if (childrenCategoriesList)
      childrenCategoriesList.classList.toggle("visible-children-categories");
    document.forms["filters-form"].reset();
    await props.productsCategory(id);
  };

  const categoriesForRender = props.categoriesList.map((value) => {
    if (!value.parentId) {
      const parentCategory = (
        <button
          onClick={(event) => handleProductsCategory(value.id, event)}
          type="button"
          className="btn btn-link p-0"
          data-test="parent-item"
        >
          {value.name}
        </button>
      );

      const childrenCategories = props.categoriesList
        .filter((children) => value.id === children.parentId)
        .map((value) => (
          <li key={value.id} data-test="children">
            <button
              onClick={(event) => handleProductsCategory(value.id, event)}
              type="button"
              data-test="child-item"
              className="btn btn-link p-0"
            >
              {value.name}
            </button>
          </li>
        ));
      if (childrenCategories.length)
        return (
          <li key={value.id} className="mb-2" data-test="parent">
            {parentCategory}
            <ul className="categories-list hidden-children-categories">
              {childrenCategories}
            </ul>
          </li>
        );
      return (
        <li key={value.id} className="mb-2" data-test="parent">
          {parentCategory}
        </li>
      );
    }
    return null;
  });

  return (
    <div className="card sidebar-card">
      <div className="card-body">
        <h5 className="card-title mb-4">Categories</h5>
        <ul className="categories-list">{categoriesForRender}</ul>
      </div>
    </div>
  );
};

export default CategorySidebar;
