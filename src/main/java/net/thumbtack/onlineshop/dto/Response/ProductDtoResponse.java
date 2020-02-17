package net.thumbtack.onlineshop.dto.Response;

import net.thumbtack.onlineshop.entities.Product;

import java.util.Collections;
import java.util.List;

public class ProductDtoResponse {
    private long id;
    private String name;
    private int price;
    private int count;
    private List<Long> categories;

    public ProductDtoResponse(long id, String name, int price, int count, List<Long> categories) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
    }

    public ProductDtoResponse(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                Collections.emptyList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Long> getCategories() {
        return categories;
    }

    public void setCategories(List<Long> categories) {
        this.categories = categories;
    }
}
