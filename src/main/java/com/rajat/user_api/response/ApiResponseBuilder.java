package com.rajat.user_api.response;

import java.util.List;

import org.springframework.data.domain.Page;

public final class ApiResponseBuilder {

    private ApiResponseBuilder() {
        // Utility class
    }

    public static <T> ApiSuccessResponse<T> success(String message, T data) {
        return new ApiSuccessResponse<>(true, message, data);
    }

    public static <T> ApiSuccessResponse<T> success(String message) {
        return new ApiSuccessResponse<>(true, message, null);
    }

    public static <T> PagedResponse<List<T>> paged(String message, Page<T> page) {

        PaginationMeta pagination = new PaginationMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );

        return new PagedResponse<>(
                true,
                message,
                page.getContent(),
                pagination
        );
    }
}