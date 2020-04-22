import CatalogContainer from "../../components/catalog/CatalogContainer";
import { getWrapper } from "../utils";

describe("testing <CatalogContainer/>", () => {
  window.HTMLElement.prototype.scrollIntoView = jest.fn();

  it("render categories of products", () => {
    const categoriesState = {
      categoriesList: [
        {
          id: 18,
          name: "Books",
        },
        {
          id: 17,
          name: "Photo equipment",
        },
        {
          id: 19,
          name: "Cameras",
          parentId: 17,
          parentName: "Photo equipment",
        },
        {
          id: 20,
          name: "Lens",
          parentId: 17,
          parentName: "Photo equipment",
        },
      ],
    };
    const wrapper = getWrapper(CatalogContainer, { categoriesState });
    const parentCategories = wrapper.find('[data-test="parent"]');
    expect(parentCategories).toHaveLength(2);
    const books = parentCategories.first();
    expect(books.find('[data-test="children"]')).toEqual({});
    expect(books.children()).toHaveLength(1);
    const photoEquipment = parentCategories.last();
    expect(photoEquipment.find('[data-test="children"]')).toHaveLength(2);
    expect(photoEquipment.children()).toHaveLength(2);
    console.log(books.find('[data-test="parent-item"]').debug());
    expect(books.find('[data-test="parent-item"]').text()).toEqual(
      categoriesState.categoriesList[0].name
    );
    expect(photoEquipment.find('[data-test="parent-item"]').text()).toEqual(
      categoriesState.categoriesList[1].name
    );
    expect(
      photoEquipment.find('[data-test="child-item"]').first().text()
    ).toEqual(categoriesState.categoriesList[2].name);
    expect(
      photoEquipment.find('[data-test="child-item"]').last().text()
    ).toEqual(categoriesState.categoriesList[3].name);
  });

  const productState = {
    productsList: [
      {
        id: 28,
        name: "Canon EF 50mm f/1.4",
        price: 370,
        count: 0,
      },
      {
        id: 31,
        name: "Eloquent JavaScript",
        price: 25,
        count: 39,
      },
      {
        id: 22,
        name: "Nikon D750",
        price: 1200,
        count: 6,
      },
      {
        id: 30,
        name: "Pro Spring 5",
        price: 60,
        count: 18,
      },
      {
        id: 29,
        name: "The C Programming Language",
        price: 40,
        count: 0,
      },
    ],
  };

  it("render products", () => {
    const wrapper = getWrapper(CatalogContainer, { productState });
    const products = wrapper.find('[data-test="item"]');
    expect(products).toHaveLength(productState.productsList.length);
    products.forEach((node, index) => {
      const product = productState.productsList[index];
      expect(node.find('[data-test="name"]').text()).toEqual(product.name);
      const price = +node.find('[data-test="price"]').text().replace(/\D/g, "");
      expect(price).toEqual(product.price);
      let stockLevel = "none";
      const count = product.count;
      if (count) stockLevel = count < 10 ? "few" : "much";
      expect(
        node.find('[data-test="count"]').text().includes(stockLevel)
      ).toBeTruthy();
      console.log(node.find(`[href="/catalog/${product.id}"]`).debug());
      expect(node.find(`[href="/catalog/${product.id}"]`)).toBeTruthy();
    });
  });

  it("render products with filters", () => {
    productState.filter = {
      isInStock: true,
      minPrice: 60,
      maxPrice: 1200,
    };
    const filteredProducts = productState.productsList.filter((value) => {
      if (!value.count) return false;
      if (value.price < productState.filter.minPrice) return false;
      return value.price <= productState.filter.maxPrice;
    });
    const wrapper = getWrapper(CatalogContainer, { productState });
    const minInput = wrapper.find('[data-test="min-price"]');
    const maxInput = wrapper.find('[data-test="max-price"]');
    expect(+minInput.instance().value).toEqual(productState.filter.minPrice);
    expect(+maxInput.instance().value).toEqual(productState.filter.maxPrice);
    wrapper.find('[name="filters-form"]').simulate("submit");
    const nodes = wrapper.find('[data-test="item"]');
    expect(nodes).toHaveLength(filteredProducts.length);
    nodes.forEach((node, index) => {
      expect(node.find('[data-test="name"]').text()).toEqual(
        filteredProducts[index].name
      );
    });
  });
});
