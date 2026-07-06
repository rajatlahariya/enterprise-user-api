package com.rajat.user_api.response;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse extends ApiSuccessResponse<Object> {

    private Map<String, String> errors;

    public ErrorResponse(boolean success, String message) {
        super(success, message, null);
    }

    public ErrorResponse(boolean success, String message, Map<String, String> errors) {
        super(success, message, null);
        this.errors = errors;
    }
}