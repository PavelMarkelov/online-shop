package net.thumbtack.onlineshop.dto.Request;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.util.Objects;

import static net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker.*;

public abstract class PersonDtoWithValid {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String login;
    private String password;

    private int maxNameLength;
    private int minPasswordLength;

    public PersonDtoWithValid(String firstName, String lastName, String patronymic,
                              String login, String password
    ) {
        this.maxNameLength = getAppProperties().get("max_name_length");
        this.minPasswordLength = getAppProperties().get("min_password_length");
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.login = login;
        this.password = password;
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
        return login.toLowerCase();
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

    @AssertTrue(message = "FirstName can't be empty and have a maximum 50 letters " +
            "and contain letters of the russian alphabet, spaces and dashes")
    public boolean isFistName() {
        return !StringUtils.isEmpty(firstName) && firstName.matches("[А-Яа-я\\s-]+")
                && firstName.length() <= maxNameLength;
    }

    @AssertTrue(message = "LastName can't be empty and have a maximum length of 50 letters " +
            "and contain letters of the russian alphabet, spaces and dashes")
    public boolean isLastName() {
        return !StringUtils.isEmpty(lastName) && lastName.matches("[А-Яа-я\\s-]+")
                && lastName.length() <= maxNameLength;
    }

    @AssertTrue(message = "Patronymic have a maximum length of 50 letters " +
            "and contain letters of the russian alphabet, spaces and dashes")
    public boolean isPatronymic() {
        if(!StringUtils.isEmpty(patronymic))
            return patronymic.matches("[А-Яа-я\\s-]+") && patronymic.length() <= maxNameLength;
        return true;
    }

    @AssertTrue(message = "Login can't be empty, have a maximum length of 50 letters " +
            "and contain only letters and numbers")
    public boolean isLogin() {
        return !StringUtils.isEmpty(login) && login.matches("[А-Яа-яA-Za-z\\d]+") && login.length() <= maxNameLength;
    }

    @AssertTrue(message = "Password can't be empty and have a minimum length of 8 letters")
    public boolean isPassword() {
        return !StringUtils.isEmpty(password) && password.matches("\\w+") && password.length() >= minPasswordLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDtoWithValid personDtoWithValid = (PersonDtoWithValid) o;
        return Objects.equals(login, personDtoWithValid.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
