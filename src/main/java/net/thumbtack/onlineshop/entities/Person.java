package net.thumbtack.onlineshop.entities;


import com.fasterxml.jackson.annotation.JsonView;
import net.thumbtack.onlineshop.dto.Request.AdminDtoWithValid;
import net.thumbtack.onlineshop.dto.Request.CustomerDtoWithValid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Id.class)
    private Long id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private String login;

    private String password;

    private String email;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Address address;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private Token token;

    private String phone;

    private int deposit;

    private String position;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "person_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    @JoinColumn(name="person_id")
    private Basket basket;

    private boolean active;
    @Column(columnDefinition="BIT NOT NULL DEFAULT true")
    private boolean accountNonExpired;
    @Column(columnDefinition="BIT NOT NULL DEFAULT true")
    private boolean accountNonLocked;
    @Column(columnDefinition="BIT NOT NULL DEFAULT true")
    private boolean credentialsNonExpired;

    @Transient
    private String tokenWithoutEncoding;

    public Person() {
    }

    public Person(AdminDtoWithValid adminDtoWithValid) {
        this.firstName = adminDtoWithValid.getFirstName();
        this.lastName = adminDtoWithValid.getLastName();
        this.patronymic = adminDtoWithValid.getPatronymic();
        this.login = adminDtoWithValid.getLogin();
        this.position = adminDtoWithValid.getPosition();
        this.active = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }

    public Person(CustomerDtoWithValid customer) {
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.patronymic = customer.getPatronymic();
        this.login = customer.getLogin();
        this.email = customer.getEmail();
        this.phone = customer.getClearPhone();
        this.deposit = 0;
        this.active = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }

    public Person(String firstName, String lastName, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
        this.deposit = 0;
        this.active = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getTokenWithoutEncoding() {
        return tokenWithoutEncoding;
    }

    public void setTokenWithoutEncoding(String tokenWithoutEncoding) {
        this.tokenWithoutEncoding = tokenWithoutEncoding;
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
                patronymic, login, email, deposit, position, roles.stream().findFirst());
    }
}
