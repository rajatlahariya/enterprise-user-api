package com.rajat.user_api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiSuccessResponse<T> {

    private boolean success;
    private String message;
    private T data;
}