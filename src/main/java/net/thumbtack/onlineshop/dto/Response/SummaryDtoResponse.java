package net.thumbtack.onlineshop.dto.Response;

import com.fasterxml.jackson.annotation.JsonView;
import net.thumbtack.onlineshop.entities.View;

public class SummaryDtoResponse {

    @JsonView(View.Data.class)
    private long totalSum;

    public SummaryDtoResponse() {
    }

    public SummaryDtoResponse(long totalSum) {
        this.totalSum = totalSum;
    }

    public long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(long totalSum) {
        this.totalSum = totalSum;
    }
}
