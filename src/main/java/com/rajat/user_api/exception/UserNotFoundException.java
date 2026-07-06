package com.rajat.user_api.exception;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}