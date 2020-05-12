import { getWrapper } from "../utils";
import React from "react";
import userRole from "../../userRole";

describe("testing <PrivateRoute/>", () => {
  const TestComponent = () => <div>test PrivateRoute</div>;

  it("should render component if user has been authenticated", () => {
    const userState = {
      isAuthenticated: true,
      user: { role: userRole.CUSTOMER },
    };
    const route = "/test";
    const wrapper = getWrapper(TestComponent, { userState }, { route }, true);
    expect(wrapper.exists(TestComponent)).toBeTruthy();
  });

  it("should redirect if user is not have admin role", () => {
    const userState = {
      isAuthenticated: true,
      user: { role: userRole.CUSTOMER },
    };
    const route = "/report";
    const wrapper = getWrapper(TestComponent, { userState }, { route }, true);
    expect(wrapper.exists(TestComponent)).toBeFalsy();
    const history = wrapper.find("Router").prop("history");
    expect(history.location.pathname).toBe("/catalog");
  });

  it("should redirect if user is not authenticated", () => {
    const userState = {
      isAuthenticated: false,
      user: { role: userRole.CUSTOMER },
    };
    const route = "/test";
    const wrapper = getWrapper(TestComponent, { userState }, { route }, true);
    expect(wrapper.exists(TestComponent)).toBeFalsy();
    const history = wrapper.find("Router").prop("history");
    expect(history.location.pathname).toBe("/");
  });
});
