package net.thumbtack.onlineshop.dto;

import net.thumbtack.onlineshop.entities.Basket;

public class BasketDto {

    private Basket bought;
    private Basket remaining;

    public BasketDto(Basket bought, Basket remaining) {
        this.bought = bought;
        this.remaining = remaining;
    }

    public Basket getBought() {
        return bought;
    }

    public void setBought(Basket bought) {
        this.bought = bought;
    }

    public Basket getRemaining() {
        return remaining;
    }

    public void setRemaining(Basket remaining) {
        this.remaining = remaining;
    }
}
