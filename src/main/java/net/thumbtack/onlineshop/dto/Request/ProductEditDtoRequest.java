package net.thumbtack.onlineshop.dto.Request;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.util.Set;

public class ProductEditDtoRequest {

    private String name;
    private Integer price;
    private Integer count;
    private Set<Long> categories;

    public ProductEditDtoRequest() {
    }

    public ProductEditDtoRequest(String name, Integer price, int count, Set<Long> categories) {
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Set<Long> getCategories() {
        return categories;
    }

    public void setCategories(Set<Long> categories) {
        this.categories = categories;
    }

    public @AssertTrue(message = "Product name can't be empty") boolean isName() {
        if (name != null)
            return !StringUtils.isEmpty(name);
        return true;
    }

    public @AssertTrue(message = "Product price can't be less than or equal to zero") boolean isPrice() {
        if (price != null)
            return price > 0;
        return true;
    }
}
