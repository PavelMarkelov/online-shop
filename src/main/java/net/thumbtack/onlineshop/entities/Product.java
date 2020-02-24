package net.thumbtack.onlineshop.entities;

import com.fasterxml.jackson.annotation.JsonView;
import net.thumbtack.onlineshop.dto.Request.ProductDtoRequest;
import net.thumbtack.onlineshop.utils.Views;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Id.class)
    private Long id;

    private String name;

    private int price;

    private int count;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private List<Category> categories = new ArrayList<>();

    public Product() {
    }

    public Product(String name, int price, int count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public Product(ProductDtoRequest response) {
        this(response.getName(), response.getPrice(), response.getCount());
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return String.format("Product: [id=%s name=%s price=%s count=%s]", id,
                name, price, count);
    }
}
