package net.thumbtack.onlineshop.dto.Request;

public class LoginDtoRequest {

    private String login;
    private String password;

    public LoginDtoRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
