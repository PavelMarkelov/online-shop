package net.thumbtack.onlineshop.dto.Request;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;

public class GetReportDtoWithValid {

    private int count;
    private String email;

    public GetReportDtoWithValid() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @AssertTrue(message = "Count can't be less than zero")
    public boolean isCount() {
        return count >= 0;
    }

    @AssertTrue(message = "Invalid format e-mail")
    public boolean isEmail() {
        return !StringUtils.isEmpty(email) &&
                email.matches("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]+$");
    }
}
