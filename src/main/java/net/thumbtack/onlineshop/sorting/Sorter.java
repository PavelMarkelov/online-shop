package net.thumbtack.onlineshop.sorting;

import net.thumbtack.onlineshop.dto.Request.SummaryDtoRequest;
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
    private static final String SORT_OPT = "product";

    private final Map<String, String[]> sortFieldOptions = new HashMap<>();
    private final List<String> timeOptions = new ArrayList<>();
    private long offset;
    private int limit;
    private Sort sort;
    private String time;

    {
        sortFieldOptions.put("count", new String[]{"count", "price", "name", "person.id"});
        sortFieldOptions.put("product", new String[]{"name", "count", "price", "person.id"});

        timeOptions.add("all");
        timeOptions.add("1");
        timeOptions.add("7");
        timeOptions.add("30");
    }

    private Sort parseSort(String order, String sortBy) {
        Sort.Direction dir = order == null || !order.equals("desc") ?
                ORDER_DEFAULT : Sort.Direction.DESC;
        String[] sortField = sortBy == null || !sortFieldOptions.containsKey(sortBy) ?
                sortFieldOptions.get(SORT_OPT) : sortFieldOptions.get(sortBy);
        return Sort.by(dir, sortField);
    }

    public Pageable updateSorting(SummaryDtoRequest values) {
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

    public static int getLIMIT() {
        return LIMIT;
    }
}
