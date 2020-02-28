package net.thumbtack.onlineshop.controller;

import com.fasterxml.jackson.annotation.JsonView;
import net.thumbtack.onlineshop.dto.Request.SummaryDtoRequest;
import net.thumbtack.onlineshop.dto.Response.SummaryDtoResponse;
import net.thumbtack.onlineshop.entities.View;
import net.thumbtack.onlineshop.service.PurchaseHistoryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.thumbtack.onlineshop.securiry.CheckAccessPerson.checkAccessAdmin;


@RestController
@RequestMapping("/purchases/")
public class SummaryListController {

    private final PurchaseHistoryService historyService;

    public SummaryListController(PurchaseHistoryService historyService) {
        this.historyService = historyService;
    }

    @JsonView(View.Data.class)
    @GetMapping
    private SummaryDtoResponse summaryList(SummaryDtoRequest request,
                                           Authentication auth) {
        checkAccessAdmin(auth);
        return historyService.getSummaryList(request);
    }
}
