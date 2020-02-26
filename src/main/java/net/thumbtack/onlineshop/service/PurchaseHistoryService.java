package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Request.HistoryListDtoRequest;
import net.thumbtack.onlineshop.dto.Response.HistoryListDtoResponse;
import net.thumbtack.onlineshop.entities.PurchaseHistory;
import net.thumbtack.onlineshop.repos.PurchaseHistoryRepository;
import net.thumbtack.onlineshop.sorting.Sorter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository historyRepository;
    private final Sorter sorter;

    public PurchaseHistoryService(PurchaseHistoryRepository historyRepository, Sorter sorter) {
        this.historyRepository = historyRepository;
        this.sorter = sorter;
    }

    public HistoryListDtoResponse getSummaryList(HistoryListDtoRequest request) {
        Pageable pageable = sorter.updateSorting(request);
        HistoryListDtoResponse response = new HistoryListDtoResponse();
        Date dateCreated = new Date(0);
        if (!sorter.getTime().equals("all")) {
            int days = Integer.parseInt(sorter.getTime());
            Calendar time = Calendar.getInstance();
            time.setTime(new Date());
            time.add(Calendar.HOUR_OF_DAY, -(days * 24));
            dateCreated = time.getTime();
        }
        if (!request.getCategory().isEmpty())
            response.setData(historyRepository
                    .findByPurchaseDateGreaterThanAndCategoriesIdIsIn(dateCreated,
                            request.getCategory(), pageable));
        else if (!request.getProduct().isEmpty())
            response.setData(historyRepository
                    .findByPurchaseDateGreaterThanAndProductIdIn(dateCreated,
                            request.getProduct(), pageable));
        else if (!request.getCustomer().isEmpty())
            response.setData(historyRepository
                    .findByPurchaseDateGreaterThanAndPersonIdIn(dateCreated,
                            request.getCustomer(), pageable));
        else
            response.setData(historyRepository
                    .findByPurchaseDateGreaterThan(dateCreated, pageable));
        List<PurchaseHistory> data = response.getData();
        data.forEach(item -> item.setTotal(item.getCount() * item.getPrice()));
        data
                .stream()
                .map(PurchaseHistory::getTotal)
                .reduce(Long::sum)
                .ifPresent(response::setTotalSum);
        response.setPageNumber(pageable.getPageNumber() + 1);
        response.setOffset(pageable.getOffset());
        response.setLimit(pageable.getPageSize());
        if (request.isTotal()) {
            response.setData(null);
        }
        return response;
    }
}
