package net.thumbtack.onlineshop.dto.Response;

public class SettingsDtoResponse {

    private int maxNameLength;
    private int minPasswordLength;

    public SettingsDtoResponse() {
    }

    public SettingsDtoResponse(int maxNameLength, int minPasswordLength) {
        this.maxNameLength = maxNameLength;
        this.minPasswordLength = minPasswordLength;
    }

    public int getMaxNameLength() {
        return maxNameLength;
    }

    public void setMaxNameLength(int maxNameLength) {
        this.maxNameLength = maxNameLength;
    }

    public int getMinPasswordLength() {
        return minPasswordLength;
    }

    public void setMinPasswordLength(int minPasswordLength) {
        this.minPasswordLength = minPasswordLength;
    }
}
