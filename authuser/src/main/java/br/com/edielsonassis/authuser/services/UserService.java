package br.com.edielsonassis.authuser.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.authuser.dtos.PageUserDto;
import br.com.edielsonassis.authuser.dtos.UserDto;
import br.com.edielsonassis.authuser.models.UserModel;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    Page<PageUserDto> findAllUsers(Integer page, Integer size, String direction, Specification<UserModel> spec);

    UserDto findUserById(UUID userId);

    String deleteUserById(UUID userId);

    UserDto updateUserById(UUID userId, UserDto userDto);

    String updateUserPasswordById(UUID userId, UserDto userDto);

    UserDto updateUserImageById(UUID userId, UserDto userDto);
}