package com.rajat.user_api.mapper;

import com.rajat.user_api.dto.request.UserPatchRequest;
import com.rajat.user_api.dto.request.UserRequest;
import com.rajat.user_api.dto.request.UserUpdateRequest;
import com.rajat.user_api.dto.response.UserResponse;
import com.rajat.user_api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setIsActive(request.getIsActive());

        return user;
    }

    public UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setAge(user.getAge());
        response.setIsActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }
    
    public void updateEntity(User user, UserUpdateRequest request) {

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setIsActive(request.getIsActive());
    }
    
    public void patchEntity(User user, UserPatchRequest request) {

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getAge() != null) {
            user.setAge(request.getAge());
        }

        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }
    }
}