package net.thumbtack.onlineshop.dto.Response;

import net.thumbtack.onlineshop.entities.Person;

public class CustomerDtoResponse extends PersonDtoResponse {

    private String email;
    private String address;
    private String phone;
    private int deposit;

    public CustomerDtoResponse(Person customer) {
        super(customer.getId(), customer.getFirstName(), customer.getLastName(),
                customer.getPatronymic()
        );
        this.email = customer.getEmail();
        this.address = customer.getAddress().getAddress();
        this.phone = customer.getPhone();
        this.deposit = customer.getDeposit();
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

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}
