package net.thumbtack.onlineshop.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product_in_basket")
public class ProductInBasket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "basket_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Basket basket;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

    private long productIdInfo;
    private String name;
    private int price;
    private int count;

    public ProductInBasket() {
    }

    public ProductInBasket(Basket basket, Product product, long productIdInfo,
                           String name, int price, int count
    ) {
        this.basket = basket;
        this.product = product;
        this.productIdInfo = productIdInfo;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getProductIdInfo() {
        return productIdInfo;
    }

    public void setProductIdInfo(long productIdInfo) {
        this.productIdInfo = productIdInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductInBasket)) return false;
        ProductInBasket that = (ProductInBasket) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("ProductInBasket: [id=%s name=%s price=%s quantity=%s]",
                id, name, price, count);
    }
}
