import { getWrapper } from "../utils";
import NavbarContainer from "../../components/templates/navbar/NavbarContainer";
import userRole from "../../userRole";

describe("testing <NavbarContainer/>", () => {
  it("rendering navbar if user is not authenticated", () => {
    const userState = {
      isAuthenticated: false,
      user: {},
    };
    const wrapper = getWrapper(NavbarContainer, { userState });
    expect(wrapper.exists("UnauthorizedNavbar")).toBeTruthy();
    expect(wrapper.exists("AuthorizedNavbar")).toBeFalsy();
  });

  it("rendering navbar if user have customer role", () => {
    const userState = {
      isAuthenticated: true,
      user: {
        firstName: "Василий",
        role: userRole.CUSTOMER,
      },
    };
    const cartState = {
      editingCart: [
        {
          id: 29,
          name: "The C Programming Language",
          price: 40,
          count: 3,
        },
      ],
    };

    const wrapper = getWrapper(NavbarContainer, { userState, cartState });
    const cartLink = wrapper.find('[data-test="cart-link"]');
    expect(cartLink.find("span").text()).toBe("1");

    console.log(wrapper.find('[data-test="account-link"]').debug());

    expect(wrapper.find('[data-test="account-link"]').find("a").text()).toBe(
      userState.user.firstName
    );
    expect(wrapper.exists('[data-test="report-link"]')).toBeFalsy();
    expect(wrapper.exists('[data-test="admin-info"]')).toBeFalsy();
  });

  it("rendering navbar if user have admin role", () => {
    const userState = {
      isAuthenticated: true,
      user: {
        firstName: "Василий",
        role: userRole.ADMIN,
      },
    };

    const wrapper = getWrapper(NavbarContainer, { userState });
    expect(wrapper.exists('[data-test="cart-link"]')).toBeFalsy();
    expect(wrapper.exists('[data-test="account-link"]')).toBeFalsy();
    expect(wrapper.exists('[data-test="report-link"]')).toBeTruthy();
    expect(wrapper.exists('[data-test="admin-info"]')).toBeTruthy();
  });
});
