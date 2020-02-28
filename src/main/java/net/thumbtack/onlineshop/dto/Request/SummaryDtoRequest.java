package net.thumbtack.onlineshop.dto.Request;

import java.util.ArrayList;
import java.util.List;

public class SummaryDtoRequest {

    private long offset;
    private int limit;
    private String order;
    private String sort;
    private String time;
    private List<Long> category;
    private List<Long> product;
    private List<Long> customer;
    private boolean total;

    {
        category = new ArrayList<>();
        product = new ArrayList<>();
        customer = new ArrayList<>();
    }

    public SummaryDtoRequest() {
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Long> getCategory() {
        return category;
    }

    public void setCategory(List<Long> category) {
        this.category = category;
    }

    public List<Long> getProduct() {
        return product;
    }

    public void setProduct(List<Long> product) {
        this.product = product;
    }

    public List<Long> getCustomer() {
        return customer;
    }

    public void setCustomer(List<Long> customer) {
        this.customer = customer;
    }

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }
}
