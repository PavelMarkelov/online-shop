package net.thumbtack.onlineshop.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

public class Sorter {

    public static Integer FIRST_PAGE = 1;
    public static Integer PAGE_SIZE_DEFAULT = 5;
    private static Sort.Direction DIRECTION_DEFAULT = Sort.Direction.ASC;

    private final List<String> sortFieldOptions = new ArrayList<>();
    private final List<Integer> pageSizeOptions = new ArrayList<>();
    private final List<String> orderOptions = new ArrayList<>();
    private final List<String> createdOptions = new ArrayList<>();
    private int offset;
    private int pageSize;
    private String sortBy;
    private Sort.Direction order;

    {
        sortFieldOptions.add("category");
        sortFieldOptions.add("product");
        sortFieldOptions.add("customer");

        pageSizeOptions.add(5);
        pageSizeOptions.add(10);
        pageSizeOptions.add(20);

        orderOptions.add(DIRECTION_DEFAULT.toString());
        orderOptions.add("desc");

        createdOptions.add("all");
        createdOptions.add("1");
        createdOptions.add("7");
        createdOptions.add("30");
    }
}
