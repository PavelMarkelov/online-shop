package net.thumbtack.onlineshop.controller;


import net.thumbtack.onlineshop.service.ClearDatabaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug/clear")
public class ClearDatabaseController {

    private final ClearDatabaseService clearDatabaseService;

    public ClearDatabaseController(ClearDatabaseService clearDatabaseService) {
        this.clearDatabaseService = clearDatabaseService;
    }

    @PostMapping
    public String clearDatabase() {
        clearDatabaseService.clear();
        return "{}";
    }
}
