package net.thumbtack.onlineshop.dto.Request;

import java.util.List;

public class GetAllProductDtoRequest {

    private List<Long> category;
    private String order;

    public GetAllProductDtoRequest() {
    }

    public List<Long> getCategory() {
        return category;
    }

    public void setCategory(List<Long> category) {
        this.category = category;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
