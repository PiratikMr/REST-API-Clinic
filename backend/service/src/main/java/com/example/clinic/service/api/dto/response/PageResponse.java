package com.example.clinic.service.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Кастомный ответ с пагинацией")
public class PageResponse<T> {

    @Schema(description = "Список элементов на текущей странице")
    private List<T> data;

    @Schema(description = "Номер текущей страницы (начиная с 0)", example = "0")
    private int currentPage;

    @Schema(description = "Количество элементов на странице", example = "20")
    private int pageSize;

    @Schema(description = "Общее количество элементов", example = "100")
    private long totalElements;

    @Schema(description = "Общее количество страниц", example = "5")
    private int totalPages;

    @Schema(description = "Поисковый запрос (если применялся)", example = "термометр")
    private String search;

    public PageResponse(Page<T> page, String search) {
        this.data = page.getContent();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.search = search;
    }
}
