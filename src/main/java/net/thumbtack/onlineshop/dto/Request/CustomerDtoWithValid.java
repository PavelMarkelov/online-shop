package net.thumbtack.onlineshop.dto.Request;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;

public class CustomerDtoWithValid extends PersonDtoWithValid {

    private String email;
    private String address;
    private String phone;

    public CustomerDtoWithValid(String firstName, String lastName, String patronymic,
                                String login, String password, String email, String address, String phone
    ) {
        super(firstName, lastName, patronymic, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClearPhone() {
        if (StringUtils.isEmpty(phone))
            return "";
        return phone.replaceAll("[()\\s-]", "");
    }

    @AssertTrue(message = "Invalid format e-mail")
    public boolean isEmail() {
        return !StringUtils.isEmpty(email) &&
                email.matches("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]+$");
    }

    @AssertTrue(message = "Address can't be empty")
    public boolean isAddress() {
        return !StringUtils.isEmpty(address);
    }

    @AssertTrue(message = "Invalid format number of phone")
    public boolean isPhone() {
        String phoneToCheck = getClearPhone();
        if (phone.matches("^8.*"))
            return phoneToCheck.matches("[0-9]{11}");
        if (phone.matches("^\\+7.*"))
            return phoneToCheck.matches("[+0-9]{12}");
        return false;
    }

}
