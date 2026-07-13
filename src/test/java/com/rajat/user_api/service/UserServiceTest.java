package com.rajat.user_api.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rajat.user_api.entity.User;
import com.rajat.user_api.exception.UserNotFoundException;
import com.rajat.user_api.mapper.UserMapper;
import com.rajat.user_api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSoftDeleteActiveUser() {

        Long userId = 101L;
        User user = new User();
        user.setId(userId);
        user.setIsActive(true);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        userService.deleteUser(userId);

        assertFalse(user.getIsActive());
        verify(userRepository).save(user);
        verify(userRepository, never()).delete(user);
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void shouldRejectSoftDeleteWhenUserDoesNotExist() {

        Long userId = 999L;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldRejectSoftDeleteWhenUserIsAlreadyInactive() {

        Long userId = 102L;
        User user = new User();
        user.setId(userId);
        user.setIsActive(false);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(userId)
        );

        verify(userRepository, never()).save(user);
    }
}
