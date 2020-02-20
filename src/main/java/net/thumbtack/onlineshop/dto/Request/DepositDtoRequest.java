package net.thumbtack.onlineshop.dto.Request;

public class DepositDtoRequest {

    private int deposit;

    public DepositDtoRequest() {
    }

    public DepositDtoRequest(int deposit) {
        this.deposit = deposit;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}
