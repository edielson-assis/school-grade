package br.com.edielsonassis.authuser.services;

import java.util.List;
import java.util.UUID;

import br.com.edielsonassis.authuser.dtos.UserDto;
import br.com.edielsonassis.authuser.models.UserModel;

public interface UserService {

    UserModel saveUser(UserDto userDto);

    List<UserModel> getAllUsers();

    UserModel getOneUser(UUID userId);

    void deleteUser(UUID userId);
}