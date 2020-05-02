package net.thumbtack.onlineshop.dto.Request;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;

public class GetReportDtoWithValid {

    private int minCount;
    private int maxCount;
    private String email;

    public GetReportDtoWithValid() {
    }


    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @AssertTrue(message = "Invalid range")
    public boolean isRange() {
        return minCount >= 0 && maxCount >= 0 && minCount <= maxCount;
    }

    @AssertTrue(message = "Invalid format e-mail")
    public boolean isEmail() {
        if (!StringUtils.isEmpty(email))
            return email.matches("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]+$");
        else return true;
    }
}
