package net.thumbtack.onlineshop.sorting;

import net.thumbtack.onlineshop.dto.Request.HistoryListDtoRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Sorter {

    private static final int OFFSET = 0;
    private static final Sort.Direction ORDER_DEFAULT = Sort.Direction.ASC;
    private static final int LIMIT = 10;
    private static final String TIME = "all";

    private final Map<String, String> sortFieldOptions = new HashMap<>();
    private final List<String> timeOptions = new ArrayList<>();
    private long offset;
    private int limit;
    private Sort sort;
    private String time;

    {
        sortFieldOptions.put("category", "category.name");
        sortFieldOptions.put("product", "product.name");

        timeOptions.add("all");
        timeOptions.add("1");
        timeOptions.add("7");
        timeOptions.add("30");
    }

    private Sort parseSort(String order, String sortBy) {
        Sort.Direction dir = order == null || order.equals("asc") ?
                ORDER_DEFAULT : Sort.Direction.DESC;
        String sortField = sortBy == null || sortBy.equals("product") ?
                sortFieldOptions.get("product") : sortFieldOptions.get("category");
        return Sort.by(dir, sortField);
    }

    public Pageable updateSorting(HistoryListDtoRequest values) {
        this.offset = values.getOffset() < 0 ? OFFSET : values.getOffset();
        this.sort = parseSort(values.getOrder(), values.getSort());
        this.limit = values.getLimit() < 1 ? LIMIT : values.getLimit();
        this.time = values.getTime() == null ||
                !timeOptions.contains(values.getTime()) ? TIME : values.getTime();
        return new CustomPageRequest(offset, limit, sort);
    }

    public long getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public Sort getSort() {
        return sort;
    }

    public String getTime() {
        return time;
    }
}
