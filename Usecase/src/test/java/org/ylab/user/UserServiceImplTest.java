package org.ylab.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ylab.entity.UserEntity;
import org.ylab.exception.BadRequestException;
import org.ylab.exception.ConflictException;
import org.ylab.exception.NotFoundException;
import org.ylab.port.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("Tests for service functionality of work with user entity")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("secret");
    }

    @Test
    @DisplayName("Test successfully create new user")
    void testCreateUserSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        UserEntity createdUser = userService.create(user);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(createdUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName("Test throw conflict exception when trying to save user with same email " +
            "that have user already saved in db")
    void testCreateUserFailure() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));

        assertThrows(ConflictException.class, () -> userService.create(user));
    }

    @Test
    @DisplayName("Test successfully return user when authenticating by proper email and password")
    void testAuthenticateUserSuccess() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));

        UserEntity authenticatedUser = userService.authenticate(user.getEmail(), user.getPassword());

        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(authenticatedUser.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    @DisplayName("Test throw not found exception when trying to authenticate by email that no saved user has")
    void testAuthenticateUserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.authenticate(user.getEmail(), user.getPassword()));
    }

    @Test
    @DisplayName("Test throw bad request exception when trying to authenticate by email and wrong password")
    void testAuthenticateUserWrongPassword() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));

        assertThrows(BadRequestException.class, () -> userService.authenticate(user.getEmail(), "wrong"));
    }
}