package net.thumbtack.onlineshop.dto.Request.editDto;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.util.Objects;

import static net.thumbtack.onlineshop.utils.propfilecheck.PropertiesFileChecker.getAppProperties;

public abstract class PersonEditDtoWithValid {

    private String firstName;
    private String lastName;
    private String patronymic;
    private String oldPassword;
    private String newPassword;

    private int maxNameLength;
    private int minPasswordLength;

    public PersonEditDtoWithValid(String firstName, String lastName, String patronymic,
                                  String oldPassword, String newPassword
    ) {
        this.maxNameLength = getAppProperties().get("max_name_length");
        this.minPasswordLength = getAppProperties().get("min_password_length");
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @AssertTrue(message = "FirstName can't be empty and have a maximum 50 of letters " +
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


    @AssertTrue(message = "Password can't be empty and have a minimum length of 8 letters")
    public boolean isNewPassword() {
        return !StringUtils.isEmpty(newPassword) && newPassword.matches("\\w+") && newPassword.length() >= minPasswordLength;
    }
}
