package com.rajat.user_api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagedResponse<T> extends ApiSuccessResponse<T> {

    private PaginationMeta pagination;

    public PagedResponse(boolean success, String message, T data, PaginationMeta pagination) {
        super(success, message, data);
        this.pagination = pagination;
    }
}