import React from "react";
import { BreadCrumbs } from "../../components/templates/BreadCrumbs";
import { render, screen } from "@testing-library/react";
import { renderWithRouter } from "../utils";

describe("testing BreadCrumbs component", () => {
  test("rendering a non active log in link", () => {
    const location = { pathname: "/" };
    const { getByText, getAllByTestId } = render(
      <BreadCrumbs location={location} product={{}} />
    );
    screen.debug(getAllByTestId(/li-.*/));
    expect(getAllByTestId(/li-.*/)).toHaveLength(1);
    expect(getByText("Log in")).toBeTruthy();
  });

  test("rendering a non active catalog link", () => {
    const location = { pathname: "/catalog" };
    const { getByText, getAllByTestId } = render(
      <BreadCrumbs location={location} product={{}} />
    );
    screen.debug(getAllByTestId(/li-.*/));
    expect(getAllByTestId(/li-.*/)).toHaveLength(1);
    expect(getByText("Catalog")).toBeTruthy();
  });

  test("rendering product link with a non active catalog link", () => {
    const location = { pathname: "/catalog/25" };
    const product = { name: "Nikon D7100" };
    const { getByText, getAllByTestId } = renderWithRouter(
      <BreadCrumbs location={location} product={product} />
    );
    const liElements = getAllByTestId(/li-.*/);
    screen.debug(liElements);
    expect(liElements).toHaveLength(2);
    expect(getByText("Catalog")).toBeTruthy();
    expect(liElements[1].textContent).toBe(product.name);
  });
});
