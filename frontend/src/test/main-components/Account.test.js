import Account from "../../components/Account";
import { getWrapper } from "../utils";

describe("testing <Account/>", () => {
  it("render account data", () => {
    const userState = {
      user: {
        firstName: "Василий",
        lastName: "Дудин",
        patronymic: "Иванович",
        email: "email@wewr.com",
        address: "4100008",
        phone: "89477645695",
        oldPassword: "sddsvwe34s",
        newPassword: "sddsvwe34s",
      },
    };
    const wrapper = getWrapper(Account, { userState });
    console.log(wrapper.find("input").debug());
    wrapper
      .find(".form-control")
      .forEach((node) => expect(node.hasClass("is-invalid")).toBeFalsy());
    expect(wrapper.find({ name: "firstName" }).instance().value).toEqual(
      userState.user.firstName
    );
    expect(wrapper.find({ name: "lastName" }).instance().value).toEqual(
      userState.user.lastName
    );
    expect(wrapper.find({ name: "patronymic" }).instance().value).toEqual(
      userState.user.patronymic
    );
    expect(wrapper.find({ name: "email" }).instance().value).toEqual(
      userState.user.email
    );
    expect(wrapper.find({ name: "address" }).instance().value).toEqual(
      userState.user.address
    );
    expect(wrapper.find({ name: "phone" }).instance().value).toEqual(
      userState.user.phone
    );
    expect(wrapper.find({ name: "oldPassword" }).instance().value).toEqual(
      userState.user.oldPassword
    );
    expect(wrapper.find({ name: "newPassword" }).instance().value).toEqual(
      userState.user.newPassword
    );
  });

  it("field errors render", () => {
    const errorState = {
      error: {
        errors: [
          {
            field: "phone",
            message: "Invalid format number of phone",
          },
          {
            field: "patronymic",
            message: "Invalid patronymic",
          },
          {
            field: "address",
            message: "Address can't be empty",
          },
          {
            field: "email",
            message: "Invalid format e-mail",
          },
          {
            field: "lastName",
            message: "Invalid lastName",
          },
          {
            field: "firstName",
            message: "Invalid firstName",
          },
          {
            field: "newPassword",
            message: "Invalid new password",
          },
        ],
      },
    };
    const wrapper = getWrapper(Account, { errorState });

    const nodes = wrapper.find(".form-control");
    nodes.forEach((node, index) => {
      if (index === nodes.length - 2)
        expect(node.hasClass("is-invalid")).toBeFalsy();
      else expect(node.hasClass("is-invalid")).toBeTruthy();
    });
    const feedBackNodes = wrapper.find(".invalid-feedback");
    console.log(feedBackNodes.at(0).debug());
    expect(feedBackNodes.first().text()).toEqual(
      errorState.error.errors[5].message
    );
    expect(feedBackNodes.last().text()).toEqual(
      errorState.error.errors[6].message
    );
  });

  it("old password error render", () => {
    const errorState = {
      error: {
        field: "oldPassword",
        message: "Old password is not valid",
      },
    };
    const wrapper = getWrapper(Account, { errorState });
    const feedBackNodes = wrapper.find(".invalid-feedback");
    expect(feedBackNodes.first().text()).toEqual("");
    expect(feedBackNodes.at(6).text()).toEqual(errorState.error.message);
  });
});
