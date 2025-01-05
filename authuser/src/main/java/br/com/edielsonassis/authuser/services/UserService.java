package br.com.edielsonassis.authuser.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.authuser.dtos.UserReponse;
import br.com.edielsonassis.authuser.dtos.UserRequest;
import br.com.edielsonassis.authuser.models.UserModel;

public interface UserService {

    UserReponse saveUser(UserRequest userDto);

    Page<UserReponse> findAllUsers(Integer page, Integer size, String direction, Specification<UserModel> spec);

    UserReponse findUserById(UUID userId);

    String deleteUserById(UUID userId);

    UserReponse updateUserById(UUID userId, UserRequest userDto);

    String updateUserPasswordById(UUID userId, UserRequest userDto);

    UserReponse updateUserImageById(UUID userId, UserRequest userDto);
}