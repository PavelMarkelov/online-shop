package net.thumbtack.onlineshop.entities;


import net.thumbtack.onlineshop.dto.AdminDto;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String login;

    private String password;

    private String email;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private Address address;

    private String phone;

    private int deposit;

    private String position;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    @JoinColumn(name="person_id")
    private Basket basket;

    public Person() {
    }

    public Person(AdminDto adminDto) {
        this.firstName = adminDto.getFirstName();
        this.lastName = adminDto.getLastName();
        this.patronymic = adminDto.getPatronymic();
        this.login = adminDto.getLogin();
        this.position = adminDto.getPosition();
        this.role = Role.ADMIN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(getId(), person.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Person: [id=%s firstName=%s lastName=%s patronymic=%s login=%s " +
                "email=%s deposit=%s position=%s role=%s]", id, firstName, lastName,
                patronymic, login, email, deposit, position, role);
    }
}
