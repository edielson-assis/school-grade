package br.com.edielsonassis.authuser.services;

import java.util.List;
import java.util.UUID;

import br.com.edielsonassis.authuser.dtos.UserDto;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    List<UserDto> findAllUsers();

    UserDto findUserById(UUID userId);

    String deleteUserById(UUID userId);

    UserDto updateUserById(UUID userId, UserDto userDto);

    String updateUserPasswordById(UUID userId, UserDto userDto);

    UserDto updateUserImageById(UUID userId, UserDto userDto);
}