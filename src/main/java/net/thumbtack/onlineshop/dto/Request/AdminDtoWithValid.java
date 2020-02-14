package net.thumbtack.onlineshop.dto.Request;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;

public class AdminDtoWithValid extends PersonDtoWithValid {

    private String position;

    public AdminDtoWithValid(String firstName, String lastName, String patronymic,
                             String login, String password, String position) {
        super(firstName, lastName, patronymic, login, password);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @AssertTrue(message = "Position can't be empty and have a maximum length of 100")
    public boolean isPosition() {
        return !StringUtils.isEmpty(position) && position.length() <= 100;
    }
}
