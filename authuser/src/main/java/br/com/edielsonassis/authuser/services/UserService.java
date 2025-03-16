package br.com.edielsonassis.authuser.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.authuser.dtos.request.UserRequest;
import br.com.edielsonassis.authuser.dtos.response.TokenAndRefreshTokenResponse;
import br.com.edielsonassis.authuser.dtos.response.TokenResponse;
import br.com.edielsonassis.authuser.dtos.response.UserResponse;
import br.com.edielsonassis.authuser.models.UserModel;

public interface UserService {

    UserResponse saveUser(UserRequest userDto);

    UserResponse saveInstructor(UserRequest userDto);

    Page<UserResponse> findAllUsers(Integer page, Integer size, String direction, Specification<UserModel> spec);

    UserResponse findUserById(UUID userId);

    UserResponse updateUser(UserRequest userDto);

    String updateUserPassword(UserRequest userDto);

    UserResponse updateUserImage(UserRequest userDto);

    TokenAndRefreshTokenResponse signin(UserRequest userDto);

    TokenResponse refreshToken(String username, String refreshToken);

    void disableUser(String email);
}