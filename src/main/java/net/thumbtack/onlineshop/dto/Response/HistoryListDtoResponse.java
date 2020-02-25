package net.thumbtack.onlineshop.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import net.thumbtack.onlineshop.entities.PurchaseHistory;
import net.thumbtack.onlineshop.entities.View;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HistoryListDtoResponse {

    @JsonView(View.Data.class)
    private int pageNumber;
    @JsonView(View.Data.class)
    private long offset;
    @JsonView(View.Data.class)
    private int limit;
    @JsonView(View.Data.class)
    private List<PurchaseHistory> data;
    @JsonView(View.Data.class)
    private long totalSum;

    public HistoryListDtoResponse() {
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<PurchaseHistory> getData() {
        return data;
    }

    public void setData(List<PurchaseHistory> data) {
        this.data = data;
    }

    public long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(long totalSum) {
        this.totalSum = totalSum;
    }
}
