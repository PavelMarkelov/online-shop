package net.thumbtack.onlineshop.service;

import net.thumbtack.onlineshop.dto.Request.SummaryDtoRequest;
import net.thumbtack.onlineshop.dto.Response.SummaryDtoResponse;
import net.thumbtack.onlineshop.dto.Response.SummaryListDtoResponse;
import net.thumbtack.onlineshop.entities.PurchaseHistory;
import net.thumbtack.onlineshop.repos.PurchaseHistoryRepository;
import net.thumbtack.onlineshop.sorting.Sorter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository historyRepository;
    private final Sorter sorter;

    public PurchaseHistoryService(PurchaseHistoryRepository historyRepository, Sorter sorter) {
        this.historyRepository = historyRepository;
        this.sorter = sorter;
    }

    public SummaryDtoResponse getSummaryList(SummaryDtoRequest request) {
        Pageable pageable = sorter.updateSorting(request);
        SummaryDtoResponse response = new SummaryDtoResponse();
        Page<PurchaseHistory> purchaseHistory;
        Date dateCreated = new Date(0);
        if (!sorter.getTime().equals("all")) {
            int days = Integer.parseInt(sorter.getTime());
            Calendar time = Calendar.getInstance();
            time.setTime(new Date());
            time.add(Calendar.HOUR_OF_DAY, -(days * 24));
            dateCreated = time.getTime();
        }
        if (!request.getCategory().isEmpty())
            purchaseHistory = historyRepository
                    .findByPurchaseDateGreaterThanAndCategoriesIdIsIn(dateCreated,
                            request.getCategory(), pageable);
        else if (!request.getProduct().isEmpty())
            purchaseHistory = historyRepository
                    .findByPurchaseDateGreaterThanAndProductIdIn(dateCreated,
                            request.getProduct(), pageable);
        else if (!request.getCustomer().isEmpty())
            purchaseHistory = historyRepository
                    .findByPurchaseDateGreaterThanAndPersonIdIn(dateCreated,
                            request.getCustomer(), pageable);
        else
            purchaseHistory = historyRepository
                    .findByPurchaseDateGreaterThan(dateCreated, pageable);
        List<PurchaseHistory> data = purchaseHistory.getContent();
        data.forEach(item -> item.setTotal(item.getCount() * item.getPrice()));
        data
                .stream()
                .map(PurchaseHistory::getTotal)
                .reduce(Long::sum)
                .ifPresent(response::setTotalSum);
        if (request.isTotal())
            return response;
        SummaryListDtoResponse responseData = new SummaryListDtoResponse();
        responseData.setTotalSum(response.getTotalSum());
        responseData.setData(data);
        responseData.setTotalPages(purchaseHistory.getTotalPages());
        responseData.setCurrentPage(purchaseHistory.getNumber() + 1);
        responseData.setTotalRecords(purchaseHistory.getTotalElements());
        return responseData;
    }
}
