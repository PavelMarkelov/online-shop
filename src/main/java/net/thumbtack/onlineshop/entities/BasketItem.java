package net.thumbtack.onlineshop.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "basket_item")
public class BasketItem {

    @EmbeddedId
    private BasketItemId basketItemId = new BasketItemId();

    @MapsId("basketId")
    @JoinColumn(name = "basket_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Basket basket;

    @MapsId("productId")
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Product product;

    private int quantity;

    public BasketItem() {
    }

    public BasketItem(Basket basket, Product product, Integer quantity) {
        this.basketItemId = new BasketItemId(basket.getId(), product.getId());
        this.basket = basket;
        this.product = product;
        this.quantity = quantity;
    }

    public BasketItemId getBasketItemId() {
        return basketItemId;
    }

    public void setBasketItemId(BasketItemId basketItemId) {
        this.basketItemId = basketItemId;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
        basketItemId.setBasketId(basket.getId());
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        basketItemId.setProductId(product.getId());
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketItem)) return false;
        BasketItem that = (BasketItem) o;
        return Objects.equals(getBasketItemId(), that.getBasketItemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBasketItemId());
    }

    @Override
    public String toString() {
        return String.format("BasketItem: [id=%s quantity=%s]", basketItemId, quantity);
    }
}
