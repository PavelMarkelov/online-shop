package net.thumbtack.onlineshop.dto.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.thumbtack.onlineshop.entities.PurchaseHistory;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HistoryListDtoResponse {

    private int totalPages;
    private int offset;
    private long limit;
    private List<PurchaseHistory> singerData;
    private long totalSum;

    public HistoryListDtoResponse() {
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public List<PurchaseHistory> getSingerData() {
        return singerData;
    }

    public void setSingerData(List<PurchaseHistory> singerData) {
        this.singerData = singerData;
    }

    public long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(long totalSum) {
        this.totalSum = totalSum;
    }
}
