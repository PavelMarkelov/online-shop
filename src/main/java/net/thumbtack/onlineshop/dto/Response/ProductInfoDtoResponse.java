package net.thumbtack.onlineshop.dto.Response;

import net.thumbtack.onlineshop.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoDtoResponse {

    private long id;
    private String name;
    private int price;
    private int count;
    private List<String> categories = new ArrayList<>();

    public ProductInfoDtoResponse() {
    }

    public ProductInfoDtoResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.count = product.getCount();
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

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
