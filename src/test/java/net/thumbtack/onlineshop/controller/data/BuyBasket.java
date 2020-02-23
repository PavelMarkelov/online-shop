package net.thumbtack.onlineshop.controller.data;

import java.util.Set;

public class BuyBasket {

    private Set<ProductInfo> bought;
    private Set<ProductInfo> remaining;

    public BuyBasket() {
    }

    public BuyBasket(Set<ProductInfo> bought, Set<ProductInfo> remaining) {
        this.bought = bought;
        this.remaining = remaining;
    }

    public Set<ProductInfo> getBought() {
        return bought;
    }

    public void setBought(Set<ProductInfo> bought) {
        this.bought = bought;
    }

    public Set<ProductInfo> getRemaining() {
        return remaining;
    }

    public void setRemaining(Set<ProductInfo> remaining) {
        this.remaining = remaining;
    }
}
