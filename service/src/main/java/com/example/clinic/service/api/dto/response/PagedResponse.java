package com.example.clinic.service.api.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
public class PagedResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private Sort sort;
    private long total;
    private int totalPages;

    public PagedResponse(Page<T> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.sort = page.getSort();
        this.total = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
