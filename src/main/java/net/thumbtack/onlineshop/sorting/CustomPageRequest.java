package net.thumbtack.onlineshop.sorting;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class CustomPageRequest implements Pageable {

    private int limit;
    private long offset;
    private final Sort sort;

    public CustomPageRequest(long offset, int limit, Sort sort) {
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return (int) (offset / limit);
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new CustomPageRequest(getOffset() + getPageSize(), getPageSize(), getSort());
    }

    public CustomPageRequest previous() {
        return hasPrevious() ? new CustomPageRequest(getOffset() - getPageSize(), getPageSize(), getSort()) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new CustomPageRequest(0, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomPageRequest)) return false;
        CustomPageRequest that = (CustomPageRequest) o;
        return limit == that.limit &&
                getOffset() == that.getOffset() &&
                Objects.equals(getSort(), that.getSort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, getOffset(), getSort());
    }
}
