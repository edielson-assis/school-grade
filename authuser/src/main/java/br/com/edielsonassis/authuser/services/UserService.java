package br.com.edielsonassis.authuser.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import br.com.edielsonassis.authuser.dtos.UserDto;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    Page<UserDto> findAllUsers(Integer page, Integer size, String direction);

    UserDto findUserById(UUID userId);

    String deleteUserById(UUID userId);

    UserDto updateUserById(UUID userId, UserDto userDto);

    String updateUserPasswordById(UUID userId, UserDto userDto);

    UserDto updateUserImageById(UUID userId, UserDto userDto);
}