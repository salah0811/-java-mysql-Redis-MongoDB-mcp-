package com.siddhu.dto;

import java.util.List;
import java.util.Map;

public class SearchResultDTO {
    private List<Map<String, Object>> records;
    private int total;
    private boolean hasMore;

    public SearchResultDTO(List<Map<String, Object>> records, int total, boolean hasMore) {
        this.records = records;
        this.total = total;
        this.hasMore = hasMore;
    }

    public List<Map<String, Object>> getRecords() {
        return records;
    }

    public int getTotal() {
        return total;
    }

    public boolean isHasMore() {
        return hasMore;
    }
}