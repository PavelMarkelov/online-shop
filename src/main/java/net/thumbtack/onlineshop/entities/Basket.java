package net.thumbtack.onlineshop.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "basket")
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Person person;

    @OneToMany(mappedBy = "basket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProductInBasket> productInBaskets = new ArrayList<>();

    public Basket() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<ProductInBasket> getProductInBaskets() {
        return productInBaskets;
    }

    public void setProductInBaskets(List<ProductInBasket> productInBaskets) {
        this.productInBaskets = productInBaskets;
    }

    public void addProduct(ProductInBasket product) {
        productInBaskets.add(product);
    }

    public void delProduct(ProductInBasket product) {
        productInBaskets.remove(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Basket)) return false;
        Basket basket = (Basket) o;
        return getId().equals(basket.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Basket: [id=%s person=%s basketItems=%s]", id, person, productInBaskets);
    }
}
