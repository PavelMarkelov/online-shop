package net.thumbtack.onlineshop.controller;

import net.thumbtack.onlineshop.dto.Request.GetAllProductDtoRequest;
import net.thumbtack.onlineshop.dto.Response.HistoryListDtoResponse;
import net.thumbtack.onlineshop.service.PurchaseHistoryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessAdmin;

@RestController
@RequestMapping("/purchases")
public class SummaryListController {

    private final PurchaseHistoryService historyService;

    public SummaryListController(PurchaseHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping()
    private HistoryListDtoResponse summaryList(GetAllProductDtoRequest request,
                                               Authentication auth) {
        checkAccessAdmin(auth);
        return null;
    }
}
