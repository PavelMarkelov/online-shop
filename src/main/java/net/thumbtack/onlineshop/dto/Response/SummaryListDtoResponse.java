package net.thumbtack.onlineshop.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import net.thumbtack.onlineshop.entities.PurchaseHistory;
import net.thumbtack.onlineshop.entities.View;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SummaryListDtoResponse extends SummaryDtoResponse {

    @JsonView(View.Data.class)
    private int totalPages;
    @JsonView(View.Data.class)
    private int currentPage;
    @JsonView(View.Data.class)
    private long totalRecords;
    @JsonView(View.Data.class)
    private List<PurchaseHistory> data;

    public SummaryListDtoResponse() {
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<PurchaseHistory> getData() {
        return data;
    }

    public void setData(List<PurchaseHistory> data) {
        this.data = data;
    }
}
