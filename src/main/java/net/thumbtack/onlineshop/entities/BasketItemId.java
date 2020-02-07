package net.thumbtack.onlineshop.entities;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BasketItemId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long basketId;
    private Long productId;

    public BasketItemId() {
    }

    public BasketItemId(Long basketId, Long productId) {
        this.basketId = basketId;
        this.productId = productId;
    }

    public Long getBasketId() {
        return basketId;
    }

    public void setBasketId(Long basketId) {
        this.basketId = basketId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketItemId)) return false;
        BasketItemId that = (BasketItemId) o;
        return Objects.equals(getBasketId(), that.getBasketId()) &&
                Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBasketId(), getProductId());
    }

    @Override
    public String toString() {
        return String.format("BasketItemId: [basketId=%s productId=%s]", basketId, productId);
    }
}
