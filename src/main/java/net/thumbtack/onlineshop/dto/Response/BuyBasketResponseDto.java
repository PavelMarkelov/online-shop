package net.thumbtack.onlineshop.dto.Response;

import java.util.List;

public class BuyBasketResponseDto {

    private List<BuyProductDtoResponse> bought;
    private List<BuyProductDtoResponse> remaining;

    public BuyBasketResponseDto() {
    }

    public BuyBasketResponseDto(List<BuyProductDtoResponse> bought, List<BuyProductDtoResponse> remaining) {
        this.bought = bought;
        this.remaining = remaining;
    }

    public List<BuyProductDtoResponse> getBought() {
        return bought;
    }

    public void setBought(List<BuyProductDtoResponse> bought) {
        this.bought = bought;
    }

    public List<BuyProductDtoResponse> getRemaining() {
        return remaining;
    }

    public void setRemaining(List<BuyProductDtoResponse> remaining) {
        this.remaining = remaining;
    }
}
