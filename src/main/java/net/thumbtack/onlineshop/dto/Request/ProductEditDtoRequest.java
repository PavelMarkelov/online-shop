package net.thumbtack.onlineshop.dto.Request;

import org.springframework.util.StringUtils;

import javax.validation.constraints.AssertTrue;
import java.util.Set;

public class ProductEditDtoRequest extends ProductDtoRequest{

    public ProductEditDtoRequest(String name, int price, int count, Set<Long> categories) {
        super(name, price, count, categories);
    }

    @Override
    public @AssertTrue(message = "Product name can't be empty") boolean isName() {
        if (super.getName() != null)
            return !StringUtils.isEmpty(super.getName());
        return true;
    }

    @Override
    public @AssertTrue(message = "Product price can't be less than or equal to zero") boolean isPrice() {
        if (super.getPrice() != 0)
            return super.getPrice() > 0;
        return true;
    }
}
