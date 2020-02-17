package net.thumbtack.onlineshop.dto.Request.editDto;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;

public class AdminEditDtoWithValid extends PersonEditDtoWithValid {

    private String position;

    public AdminEditDtoWithValid(String firstName, String lastName, String patronymic,
                                 String oldPassword, String newPassword, String position) {
        super(firstName, lastName, patronymic, oldPassword, newPassword);
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
