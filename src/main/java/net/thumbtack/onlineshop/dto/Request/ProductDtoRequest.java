package net.thumbtack.onlineshop.dto.Request;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.util.Set;

public class ProductDtoRequest {

    private String name;
    private Integer price;
    private int count;
    private Set<Long> categories;

    public ProductDtoRequest() {
    }

    public ProductDtoRequest(String name, int price, int count, Set<Long> categories) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.categories = categories;
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

    public Set<Long> getCategories() {
        return categories;
    }

    public void setCategories(Set<Long> categories) {
        this.categories = categories;
    }

    @AssertTrue(message = "Product name can't be empty")
    public boolean isName() {
        return !StringUtils.isEmpty(name);
    }

    @AssertTrue(message = "Product price can't be less than or equal to zero")
    public boolean isPrice() {
        return price > 0;
    }
}
