package net.thumbtack.onlineshop.dto;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;

public class AdminDto extends PersonDTO {

    private String position;

    public AdminDto(String firstName, String lastName, String patronymic,
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
    public boolean isPositionTrue() {
        return !StringUtils.isEmpty(position) && position.length() <= 100;
    }
}
