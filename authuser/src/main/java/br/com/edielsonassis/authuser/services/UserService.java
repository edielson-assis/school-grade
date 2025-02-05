package br.com.edielsonassis.authuser.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.authuser.dtos.request.UserRequest;
import br.com.edielsonassis.authuser.dtos.response.UserResponse;
import br.com.edielsonassis.authuser.models.UserModel;

public interface UserService {

    UserResponse saveUser(UserRequest userDto);

    Page<UserResponse> findAllUsers(Integer page, Integer size, String direction, Specification<UserModel> spec);

    UserResponse findUserById(UUID userId);

    String deleteUserById(UUID userId);

    UserResponse updateUserById(UUID userId, UserRequest userDto);

    String updateUserPasswordById(UUID userId, UserRequest userDto);

    UserResponse updateUserImageById(UUID userId, UserRequest userDto);
}