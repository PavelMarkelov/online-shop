package net.thumbtack.onlineshop.dto.Response;

import net.thumbtack.onlineshop.entities.Person;

public class AdminDtoResponse extends PersonDtoResponse {

    private String position;

    public AdminDtoResponse(Person admin) {
        super(admin.getId(), admin.getFirstName(), admin.getLastName(), admin.getPatronymic());
        this.position = admin.getPosition();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
